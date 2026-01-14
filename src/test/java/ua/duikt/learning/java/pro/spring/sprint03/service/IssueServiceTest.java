<<<<<<<< HEAD:src/test/java/ua/duikt/learning/java/pro/spring/sprint02/service/IssueServiceTest.java
package ua.duikt.learning.java.pro.spring.sprint02.service;
========
package ua.duikt.learning.java.pro.spring.sprint03.service;
>>>>>>>> refs/heads/task-sprint-3:src/test/java/ua/duikt/learning/java/pro/spring/sprint03/service/IssueServiceTest.java

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
import ua.duikt.learning.java.pro.spring.service.StatusService;
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
<<<<<<<< HEAD:src/test/java/ua/duikt/learning/java/pro/spring/sprint02/service/IssueServiceTest.java
    @DisplayName("Status Management")
    void statusManagement() {
        issueService.createStatus(1, "To Do", StatusCategory.TO_DO);
        issueService.createStatus(1, "In Progress", StatusCategory.IN_PROGRESS);

        List<Status> statuses = issueService.getStatuses(1);
        assertThat(statuses).hasSize(2);
        assertThat(statuses.get(0).getName()).isEqualTo("To Do");
    }

    @Test
    @DisplayName("Status Lifecycle: Create, Update, Delete")
    void statusLifecycle() {
        Integer sId = issueService.createStatus(1, "To Do", StatusCategory.TO_DO);

        List<Status> statuses = issueService.getStatuses(1);
        assertThat(statuses).hasSize(1);
        assertThat(statuses.get(0).getName()).isEqualTo("To Do");

        issueService.updateStatus(sId, "To Do Updated");

        Status updatedStatus = issueService.getStatuses(1).get(0);
        assertThat(updatedStatus.getName()).isEqualTo("To Do Updated");

        boolean deleted = issueService.deleteStatus(sId);
        assertThat(deleted).isTrue();
        assertThat(issueService.getStatuses(1)).isEmpty();
    }

    @Test
========
>>>>>>>> refs/heads/task-sprint-3:src/test/java/ua/duikt/learning/java/pro/spring/sprint03/service/IssueServiceTest.java
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

        assertThat(history.size()).isGreaterThanOrEqualTo(1);

        IssueHistory lastChange = history.get(history.size() - 1);
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

        List<IssueHistory> history = issueService.getHistory(issueId);
        boolean hasAssigneeRecord = history.stream()
                .anyMatch(h -> "assignee".equals(h.getFieldChanged()) && String.valueOf(assigneeId).equals(h.getNewValue()));

    }
}
