package ua.duikt.learning.java.pro.spring.service.impl;

import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;
import ua.duikt.learning.java.pro.spring.service.IssueService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
// TODO: Implements all necessary methods
public class IssueServiceImpl implements IssueService {
    // TODO: Implements the method
    @Override
    public Long createIssue(Long projectId, String title, String description, IssueType type, Priority priority, Long statusId) {
        return 0L;
    }

    // TODO: Implements the method
    @Override
    public Issue getIssue(Long id) {
        return null;
    }

    // TODO: Implements the method
    @Override
    public List<Issue> listIssues(Long projectId) {
        return List.of();
    }

    // TODO: Implements the method
    @Override
    public void updateIssue(Long id, String title, String description) {

    }

    // TODO: Implements the method
    @Override
    public boolean deleteIssue(Long id) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public void patchStatus(Long id, Long newStatusId) {

    }

    // TODO: Implements the method
    @Override
    public void patchAssignee(Long id, Long assigneeId) {

    }

    // TODO: Implements the method
    @Override
    public Long createStatus(Long projectId, String name, StatusCategory category) {
        return 0L;
    }

    // TODO: Implements the method
    @Override
    public List<Status> getStatuses(Long projectId) {
        return List.of();
    }

    // TODO: Implements the method
    @Override
    public void updateStatus(Long id, String name) {

    }

    // TODO: Implements the method
    @Override
    public boolean deleteStatus(Long id) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public List<IssueHistory> getHistory(Long issueId) {
        return List.of();
    }

    // TODO: Implements the method
    private void recordHistory(Long issueId, String field, String oldVal, String newVal) {

    }
}
