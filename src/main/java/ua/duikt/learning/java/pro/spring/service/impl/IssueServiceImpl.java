package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.repositories.IssueHistoryRepo;
import ua.duikt.learning.java.pro.spring.repositories.IssueRepo;
import ua.duikt.learning.java.pro.spring.repositories.ProjectRepo;
import ua.duikt.learning.java.pro.spring.repositories.StatusRepo;
import ua.duikt.learning.java.pro.spring.service.IssueService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepo issueRepo;
    private final IssueHistoryRepo issueHistoryRepo;
    private final ProjectRepo projectRepo;
    private final StatusRepo statusRepo;

    @Override
    @Transactional
    public Long createIssue(Long projectId, String title, String description, IssueType type, Priority priority, Long statusId) {
        log.info("Creating issue in ProjectID: {}, Title: '{}'", projectId, title);
        if (!projectRepo.existsById(projectId)) {
            log.error("Project ID: {} not found when creating issue", projectId);
            throw new ResourceNotFoundException("Project does not exists");
        }
        if (!statusRepo.existsById(statusId)) {
            log.error("Status ID: {} not found when creating issue", statusId);
            throw new ResourceNotFoundException("Status does not exists");
        }

        Issue issue = Issue.builder()
                .projectId(projectId)
                .title(title)
                .description(description)
                .type(type)
                .priority(priority)
                .statusId(statusId)
                .issueKey("ISSUE-" + UUID.randomUUID().toString().substring(0, 8))
                .createdAt(LocalDateTime.now())
                .build();

        Issue savedIssue = issueRepo.save(issue);
        log.info("Issue created successfully with ID: {} and Key: {}", savedIssue.getId(), savedIssue.getIssueKey());

        recordHistory(savedIssue.getId(), "creation", null, "created");

        return savedIssue.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssue(Long id) {
        log.info("Fetching issue ID: {}", id);
        return issueRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Issue ID: {} not found", id);
                    return new ResourceNotFoundException("Issue does not exists");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> listIssues(Long projectId) {
        log.info("Listing issues for ProjectID: {}", projectId);
        return issueRepo.findAllByProjectId(projectId);
    }

    @Override
    @Transactional
    public void updateIssue(Long id, String title, String description) {
        log.info("Updating issue ID: {}", id);

        Issue issue = issueRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Issue ID: {} not found for update", id);
                    return new ResourceNotFoundException("Issue with id " + id + " not found");
                });

        issue.setTitle(title);
        issue.setDescription(description);
        issue.setUpdatedAt(LocalDateTime.now());

        recordHistory(issue.getId(), "details", "old", "updated");
        log.info("Issue ID: {} updated successfully", id);
    }


    @Override
    @Transactional
    public void deleteIssue(Long id) {
        log.info("Deleting issue ID: {}", id);
        if (!issueRepo.existsById(id)) {
            log.error("Issue ID: {} not found for deletion", id);
            throw new ResourceNotFoundException("Issue with id " + id + " not found");
        }
        issueRepo.deleteById(id);
        log.info("Issue ID: {} deleted successfully", id);
    }

    @Override
    @Transactional
    public void patchStatus(Long id, Long newStatusId) {
        log.info("Changing status for issue ID: {} to StatusID: {}", id, newStatusId);

        Issue issue = issueRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Issue ID: {} not found for status patch", id);
                    return new ResourceNotFoundException("Issue not found id=" + id);
                });

        String oldStatus = String.valueOf(issue.getStatusId());
        issue.setStatusId(newStatusId);
        issue.setUpdatedAt(LocalDateTime.now());

        recordHistory(issue.getId(), "status", oldStatus, String.valueOf(newStatusId));
        log.info("Status updated successfully for issue ID: {}", id);
    }

    @Override
    @Transactional
    public void patchAssignee(Long id, Long assigneeId) {
        log.info("Changing assignee for issue ID: {} to UserID: {}", id, assigneeId);

        Issue issue = issueRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Issue ID: {} not found for assignee patch", id);
                    return new ResourceNotFoundException("Issue not found id=" + id);
                });

        String oldAssignee = String.valueOf(issue.getAssigneeId());
        issue.setAssigneeId(assigneeId);
        issue.setUpdatedAt(LocalDateTime.now());

        recordHistory(issue.getId(), "assignee", oldAssignee, String.valueOf(assigneeId));
        log.info("Assignee updated successfully for issue ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueHistory> getHistory(Long issueId) {
        log.info("Fetching history for issue ID: {}", issueId);
        return issueHistoryRepo.findAllByIssueId(issueId);
    }

    private void recordHistory(Long issueId, String field, String oldVal, String newVal) {
        log.debug("Recording history for issue ID: {} - Field: {}, Old: {}, New: {}", issueId, field, oldVal, newVal);
        IssueHistory history = IssueHistory.builder()
                .issueId(issueId)
                .fieldChanged(field)
                .oldValue(oldVal)
                .newValue(newVal)
                .createdAt(LocalDateTime.now())
                .build();

        issueHistoryRepo.save(history);
    }

}