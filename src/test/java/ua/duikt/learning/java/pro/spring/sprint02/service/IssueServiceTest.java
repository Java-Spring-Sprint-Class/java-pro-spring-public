package ua.duikt.learning.java.pro.spring.sprint02.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;
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
    @DisplayName("Status Management")
    void statusManagement() {
        issueService.createStatus(1L, "To Do", StatusCategory.TO_DO);
        issueService.createStatus(1L, "In Progress", StatusCategory.IN_PROGRESS);

        List<Status> statuses = issueService.getStatuses(1L);
        assertThat(statuses).hasSize(2);
        assertThat(statuses.getFirst().getName()).isEqualTo("To Do");
    }

    @Test
    @DisplayName("Status Lifecycle: Create, Update, Delete")
    void statusLifecycle() {
        Long sId = issueService.createStatus(1L, "To Do", StatusCategory.TO_DO);

        List<Status> statuses = issueService.getStatuses(1L);
        assertThat(statuses).hasSize(1);
        assertThat(statuses.getFirst().getName()).isEqualTo("To Do");

        issueService.updateStatus(sId, "To Do Updated");

        Status updatedStatus = issueService.getStatuses(1L).getFirst();
        assertThat(updatedStatus.getName()).isEqualTo("To Do Updated");

        boolean deleted = issueService.deleteStatus(sId);
        assertThat(deleted).isTrue();
        assertThat(issueService.getStatuses(1L)).isEmpty();
    }

    @Test
    @DisplayName("Issue CRUD: Create, Get, Update, Delete")
    void issueCrud() {
        Long sId = issueService.createStatus(1L, "To Do", StatusCategory.TO_DO);
        Long issueId = issueService.createIssue(1L, "Login Bug", "Fix it", IssueType.BUG, Priority.HIGH, sId);

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
        Long sId = issueService.createStatus(1L, "To Do", StatusCategory.TO_DO);
        issueService.createIssue(1L, "Task 1", "Desc", IssueType.TASK, Priority.LOW, sId);
        issueService.createIssue(1L, "Task 2", "Desc", IssueType.STORY, Priority.MEDIUM, sId);

        issueService.createIssue(2L, "Other Project Task", "Desc", IssueType.TASK, Priority.LOW, sId);

        List<Issue> project1Issues = issueService.listIssues(1L);
        assertThat(project1Issues).hasSize(2);

        List<Issue> project2Issues = issueService.listIssues(2L);
        assertThat(project2Issues).hasSize(1);
    }

    @Test
    @DisplayName("Patch Status and History Tracking")
    void patchStatusAndHistory() {
        Long statusId = 100L;
        Long issueId = issueService.createIssue(1L, "Task 1", "Desc", IssueType.TASK, Priority.MEDIUM, statusId);

        issueService.patchStatus(issueId, statusId);

        Issue updatedIssue = issueService.getIssue(issueId);
        assertThat(updatedIssue.getStatusId()).isEqualTo(statusId);

        List<IssueHistory> history = issueService.getHistory(issueId);

        assertThat(history.size()).isGreaterThanOrEqualTo(1);

        IssueHistory lastChange = history.getLast();
        assertThat(lastChange.getFieldChanged()).isEqualTo("status");
        assertThat(lastChange.getNewValue()).isEqualTo(String.valueOf(statusId));
    }

    @Test
    @DisplayName("Patch Assignee and History")
    void patchAssignee() {
        Long statusId = 100L;
        Long issueId = issueService.createIssue(1L, "Task 1", "Desc", IssueType.TASK, Priority.MEDIUM, statusId);
        Long assigneeId = 55L;

        issueService.patchAssignee(issueId, assigneeId);

        assertThat(issueService.getIssue(issueId).getAssigneeId()).isEqualTo(assigneeId);
    }
}
