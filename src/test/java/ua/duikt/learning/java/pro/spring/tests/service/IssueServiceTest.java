package ua.duikt.learning.java.pro.spring.tests.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.repositories.IssueHistoryRepo;
import ua.duikt.learning.java.pro.spring.repositories.IssueRepo;
import ua.duikt.learning.java.pro.spring.service.impl.IssueServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueRepo issueRepo;

    @Mock
    private IssueHistoryRepo issueHistoryRepo;

    @InjectMocks
    private IssueServiceImpl issueService;

    @Test
    @DisplayName("createIssue: should save issue, record history and return id")
    void createIssue_shouldSaveIssueAndRecordHistory() {
        
        Issue savedIssue = Issue.builder()
                .id(1L)
                .build();

        when(issueRepo.save(any(Issue.class))).thenReturn(savedIssue);

        ArgumentCaptor<Issue> issueCaptor = ArgumentCaptor.forClass(Issue.class);
        ArgumentCaptor<IssueHistory> historyCaptor = ArgumentCaptor.forClass(IssueHistory.class);

        
        Long result = issueService.createIssue(
                10L,
                "Test title",
                "Test description",
                IssueType.BUG,
                Priority.HIGH,
                1L
        );

        
        assertThat(result).isEqualTo(1L);

        verify(issueRepo).save(issueCaptor.capture());
        verify(issueHistoryRepo).save(historyCaptor.capture());

        Issue issue = issueCaptor.getValue();
        assertThat(issue.getProjectId()).isEqualTo(10L);
        assertThat(issue.getTitle()).isEqualTo("Test title");
        assertThat(issue.getType()).isEqualTo(IssueType.BUG);
        assertThat(issue.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(issue.getIssueKey()).startsWith("ISSUE-");
        assertThat(issue.getCreatedAt()).isNotNull();

        IssueHistory history = historyCaptor.getValue();
        assertThat(history.getIssueId()).isEqualTo(1L);
        assertThat(history.getFieldChanged()).isEqualTo("creation");
        assertThat(history.getNewValue()).isEqualTo("created");
        assertThat(history.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("getIssue: should return issue when found")
    void getIssue_shouldReturnIssue() {
        
        Issue issue = Issue.builder().id(1L).build();
        when(issueRepo.findById(1L)).thenReturn(Optional.of(issue));

        
        Issue result = issueService.getIssue(1L);

        
        assertThat(result).isNotNull();
        verify(issueRepo).findById(1L);
    }

    @Test
    @DisplayName("getIssue: should return null when not found")
    void getIssue_shouldReturnNullIfNotFound() {
        
        when(issueRepo.findById(99L)).thenReturn(Optional.empty());

        
        Issue result = issueService.getIssue(99L);

        
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("listIssues: should return issues by projectId")
    void listIssues_shouldReturnIssuesByProjectId() {
        
        when(issueRepo.findAllByProjectId(5L))
                .thenReturn(List.of(new Issue(), new Issue()));

        
        List<Issue> result = issueService.listIssues(5L);

        
        assertThat(result).hasSize(2);
        verify(issueRepo).findAllByProjectId(5L);
    }

    @Test
    @DisplayName("updateIssue: should update fields and record history if issue exists")
    void updateIssue_shouldUpdateAndRecordHistory() {
        
        Issue issue = Issue.builder()
                .id(1L)
                .title("Old")
                .description("Old desc")
                .build();

        when(issueRepo.findById(1L)).thenReturn(Optional.of(issue));

        
        issueService.updateIssue(1L, "New title", "New desc");

        
        assertThat(issue.getTitle()).isEqualTo("New title");
        assertThat(issue.getDescription()).isEqualTo("New desc");
        assertThat(issue.getUpdatedAt()).isNotNull();

        verify(issueHistoryRepo).save(any(IssueHistory.class));
    }

    @Test
    @DisplayName("updateIssue: should do nothing when issue does not exist")
    void updateIssue_shouldDoNothingIfNotExists() {
        
        when(issueRepo.findById(99L)).thenReturn(Optional.empty());

        
        issueService.updateIssue(99L, "x", "y");

        
        verify(issueHistoryRepo, never()).save(any());
    }

    @Test
    @DisplayName("deleteIssue: should delete issue and return true if exists")
    void deleteIssue_shouldDeleteAndReturnTrue() {
        
        when(issueRepo.existsById(1L)).thenReturn(true);

        
        boolean result = issueService.deleteIssue(1L);

        
        assertThat(result).isTrue();
        verify(issueRepo).deleteById(1L);
    }

    @Test
    @DisplayName("deleteIssue: should return false if issue does not exist")
    void deleteIssue_shouldReturnFalseIfNotExists() {
        
        when(issueRepo.existsById(1L)).thenReturn(false);

        
        boolean result = issueService.deleteIssue(1L);

        
        assertThat(result).isFalse();
        verify(issueRepo, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("patchStatus: should update status and record history")
    void patchStatus_shouldUpdateAndRecordHistory() {
        
        Issue issue = Issue.builder().id(1L).statusId(1L).build();
        when(issueRepo.findById(1L)).thenReturn(Optional.of(issue));

        
        issueService.patchStatus(1L, 2L);

        
        assertThat(issue.getStatusId()).isEqualTo(2L);
        verify(issueHistoryRepo).save(any(IssueHistory.class));
    }

    @Test
    @DisplayName("patchAssignee: should update assignee and record history")
    void patchAssignee_shouldUpdateAndRecordHistory() {
        
        Issue issue = Issue.builder().id(1L).assigneeId(10L).build();
        when(issueRepo.findById(1L)).thenReturn(Optional.of(issue));

        
        issueService.patchAssignee(1L, 20L);

        
        assertThat(issue.getAssigneeId()).isEqualTo(20L);
        verify(issueHistoryRepo).save(any(IssueHistory.class));
    }

    @Test
    @DisplayName("getHistory: should return issue history")
    void getHistory_shouldReturnHistory() {
        
        when(issueHistoryRepo.findAllByIssueId(1L))
                .thenReturn(List.of(new IssueHistory(), new IssueHistory()));

        
        List<IssueHistory> result = issueService.getHistory(1L);

        
        assertThat(result).hasSize(2);
        verify(issueHistoryRepo).findAllByIssueId(1L);
    }
}
