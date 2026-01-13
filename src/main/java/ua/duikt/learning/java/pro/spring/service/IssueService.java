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
    Integer createIssue(
            Integer projectId,
            String title,
            String description,
            IssueType type,
            Priority priority
    );

    Issue getIssue(Integer id);

    List<Issue> listIssues(Integer projectId);

    void updateIssue(Integer id, String title, String description);

    boolean deleteIssue(Integer id);

    void patchStatus(Integer id, Integer newStatusId);

    void patchAssignee(Integer id, Integer assigneeId);

    // ===== Statuses =====

    Integer createStatus(Integer projectId, String name, StatusCategory category);

    List<Status> getStatuses(Integer projectId);

    void updateStatus(Integer id, String name);

    boolean deleteStatus(Integer id);

    // ===== History =====

    List<IssueHistory> getHistory(Integer issueId);
}
