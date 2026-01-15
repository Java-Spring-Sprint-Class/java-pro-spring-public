package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.repositories.IssueHistoryRepo;
import ua.duikt.learning.java.pro.spring.repositories.IssueRepo;
import ua.duikt.learning.java.pro.spring.service.IssueService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepo issueRepo;
    private final IssueHistoryRepo issueHistoryRepo;

    @Override
    @Transactional
    public Long createIssue(Long projectId, String title, String description, IssueType type, Priority priority, Long statusId) {
        Issue issue = Issue.builder()
                .projectId(projectId)
                .title(title)
                .description(description)
                .type(type)
                .priority(priority)
                .statusId(statusId)
                .key("ISSUE-" + UUID.randomUUID().toString().substring(0, 8))
                .createdAt(LocalDateTime.now())
                .build();

        Issue savedIssue = issueRepo.save(issue);

        recordHistory(savedIssue.getId(), "creation", null, "created");

        return savedIssue.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssue(Long id) {
        return issueRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> listIssues(Long projectId) {
        return issueRepo.findAllByProjectId(projectId);
    }

    @Override
    @Transactional
    public void updateIssue(Long id, String title, String description) {
        issueRepo.findById(id).ifPresent(issue -> {
            issue.setTitle(title);
            issue.setDescription(description);
            issue.setUpdatedAt(LocalDateTime.now());

            recordHistory(id, "details", "old", "updated");
        });
    }

    @Override
    @Transactional
    public boolean deleteIssue(Long id) {
        if (issueRepo.existsById(id)) {
            issueRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void patchStatus(Long id, Long newStatusId) {
        issueRepo.findById(id).ifPresent(issue -> {
            String oldStatus = String.valueOf(issue.getStatusId());
            issue.setStatusId(newStatusId);
            issue.setUpdatedAt(LocalDateTime.now());

            recordHistory(id, "status", oldStatus, String.valueOf(newStatusId));
        });
    }

    @Override
    @Transactional
    public void patchAssignee(Long id, Long assigneeId) {
        issueRepo.findById(id).ifPresent(issue -> {
            String oldAssignee = String.valueOf(issue.getAssigneeId());
            issue.setAssigneeId(assigneeId);
            issue.setUpdatedAt(LocalDateTime.now());

            recordHistory(id, "assignee", oldAssignee, String.valueOf(assigneeId));
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueHistory> getHistory(Long issueId) {
        return issueHistoryRepo.findAllByIssueId(issueId);
    }

    private void recordHistory(Long issueId, String field, String oldVal, String newVal) {
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