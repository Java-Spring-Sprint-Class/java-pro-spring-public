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
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.repositories.IssueHistoryRepo;
import ua.duikt.learning.java.pro.spring.repositories.IssueRepo;
import ua.duikt.learning.java.pro.spring.repositories.ProjectRepo;
import ua.duikt.learning.java.pro.spring.repositories.StatusRepo;
import ua.duikt.learning.java.pro.spring.service.impl.IssueServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private StatusRepo statusRepo;

    @Mock
    private IssueRepo issueRepo;

    @Mock
    private IssueHistoryRepo issueHistoryRepo;

    @InjectMocks
    private IssueServiceImpl issueService;

    @Test
    @DisplayName("createIssue: should save issue, record history and return id")
    void createIssue_shouldSaveIssueAndRecordHistory() {
        Long projectId = 10L;
        Long statusId = 1L;

        when(projectRepo.existsById(projectId)).thenReturn(true);
        when(statusRepo.existsById(statusId)).thenReturn(true);

        Issue savedIssue = Issue.builder()
                .id(1L)
                .build();

        when(issueRepo.save(any(Issue.class))).thenReturn(savedIssue);

        ArgumentCaptor<Issue> issueCaptor = ArgumentCaptor.forClass(Issue.class);
        ArgumentCaptor<IssueHistory> historyCaptor = ArgumentCaptor.forClass(IssueHistory.class);

        Long result = issueService.createIssue(
                projectId,
                "Test title",
                "Test description",
                IssueType.BUG,
                Priority.HIGH,
                statusId
        );

        assertThat(result).isEqualTo(1L);

        verify(projectRepo).existsById(projectId);
        verify(statusRepo).existsById(statusId);

        verify(issueRepo).save(issueCaptor.capture());
        verify(issueHistoryRepo).save(historyCaptor.capture());

        Issue issue = issueCaptor.getValue();
        assertThat(issue.getProjectId()).isEqualTo(projectId);
        assertThat(issue.getTitle()).isEqualTo("Test title");
        assertThat(issue.getStatusId()).isEqualTo(statusId);

        IssueHistory history = historyCaptor.getValue();
        assertThat(history.getIssueId()).isEqualTo(1L);
        assertThat(history.getFieldChanged()).isEqualTo("creation");
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
    @DisplayName("getIssue: should throw ResourceNotFoundException when not found")
    void getIssue_shouldThrowException_ifNotFound() {
        Long issueId = 99L;
        when(issueRepo.findById(issueId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> issueService.getIssue(issueId));
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
    @DisplayName("updateIssue: should throw ResourceNotFoundException when issue does not exist")
    void updateIssue_shouldThrowException_ifNotExists() {
        Long issueId = 99L;
        when(issueRepo.findById(issueId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            issueService.updateIssue(issueId, "New Title", "New Description");
        });

        verify(issueHistoryRepo, never()).save(any());
    }

    @Test
    @DisplayName("deleteIssue: should delete issue if exists")
    void deleteIssue_shouldDelete_ifExists() {
        Long issueId = 1L;
        when(issueRepo.existsById(issueId)).thenReturn(true);

        issueService.deleteIssue(issueId);

        verify(issueRepo).deleteById(issueId);
    }

    @Test
    @DisplayName("deleteIssue: should throw ResourceNotFoundException if issue does not exist")
    void deleteIssue_shouldThrowResourceNotFoundException_ifNotExists() {
        Long issueId = 1L;
        when(issueRepo.existsById(issueId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            issueService.deleteIssue(issueId);
        });

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
