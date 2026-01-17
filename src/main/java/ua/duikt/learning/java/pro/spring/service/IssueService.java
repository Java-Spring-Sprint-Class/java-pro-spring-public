package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface IssueService {
    // ===== Issues =====
    Long createIssue(
            Long projectId,
            String title,
            String description,
            IssueType type,
            Priority priority,
            Long statusId
    );

    Issue getIssue(Long id);

    List<Issue> listIssues(Long projectId);

    void updateIssue(Long id, String title, String description);

    void deleteIssue(Long id);

    void patchStatus(Long id, Long newStatusId);

    void patchAssignee(Long id, Long assigneeId);

    // ===== History =====
    List<IssueHistory> getHistory(Long issueId);
}
