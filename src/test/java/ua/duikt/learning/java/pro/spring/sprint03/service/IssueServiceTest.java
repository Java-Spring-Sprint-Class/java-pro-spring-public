package ua.duikt.learning.java.pro.spring.sprint03.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.service.IssueService;
import ua.duikt.learning.java.pro.spring.service.impl.IssueServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
class IssueServiceTest {

    private IssueService issueService;

    @BeforeEach
    void setUp() {
        issueService = new IssueServiceImpl();
    }

    @Test
    @DisplayName("Issue CRUD: Create, Get, Update, Delete")
    void issueCrud() {
        Integer issueId = issueService.createIssue(1, "Login Bug", "Fix it", IssueType.BUG, Priority.HIGH);

        Issue issue = issueService.getIssue(issueId);
        assertThat(issue).isNotNull();
        assertThat(issue.getTitle()).isEqualTo("Login Bug");

        issueService.updateIssue(issueId, "Login Bug Fixed", "It is done");

        Issue updatedIssue = issueService.getIssue(issueId);
        assertThat(updatedIssue.getTitle()).isEqualTo("Login Bug Fixed");
        assertThat(updatedIssue.getDescription()).isEqualTo("It is done");

        boolean isDeleted = issueService.deleteIssue(issueId);
        assertThat(isDeleted).isTrue();
        assertThat(issueService.getIssue(issueId)).isNull();
    }

    @Test
    @DisplayName("List Issues by Project")
    void listIssues() {
        issueService.createIssue(1, "Task 1", "Desc", IssueType.TASK, Priority.LOW);
        issueService.createIssue(1, "Task 2", "Desc", IssueType.STORY, Priority.MEDIUM);

        issueService.createIssue(2, "Other Project Task", "Desc", IssueType.TASK, Priority.LOW);

        List<Issue> project1Issues = issueService.listIssues(1);
        assertThat(project1Issues).hasSize(2);

        List<Issue> project2Issues = issueService.listIssues(2);
        assertThat(project2Issues).hasSize(1);
    }

    @Test
    @DisplayName("Patch Status and History Tracking")
    void patchStatusAndHistory() {
        Integer issueId = issueService.createIssue(1, "Task 1", "Desc", IssueType.TASK, Priority.MEDIUM);
        Integer statusId = 100;

        issueService.patchStatus(issueId, statusId);

        Issue updatedIssue = issueService.getIssue(issueId);
        assertThat(updatedIssue.getStatusId()).isEqualTo(statusId);

        List<IssueHistory> history = issueService.getHistory(issueId);

        assertThat(history).hasSizeGreaterThanOrEqualTo(1);

        IssueHistory lastChange = history.getLast();
        assertThat(lastChange.getFieldChanged()).isEqualTo("status");
        assertThat(lastChange.getNewValue()).isEqualTo(String.valueOf(statusId));
    }

    @Test
    @DisplayName("Patch Assignee and History")
    void patchAssignee() {
        Integer issueId = issueService.createIssue(1, "Task 1", "Desc", IssueType.TASK, Priority.MEDIUM);
        Integer assigneeId = 55;

        issueService.patchAssignee(issueId, assigneeId);

        assertThat(issueService.getIssue(issueId).getAssigneeId()).isEqualTo(assigneeId);
    }
}
