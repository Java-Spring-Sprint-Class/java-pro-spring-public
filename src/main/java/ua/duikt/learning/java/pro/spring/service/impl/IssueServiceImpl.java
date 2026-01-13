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
    public Integer createIssue(Integer projectId, String title, String description, IssueType type, Priority priority) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public Issue getIssue(Integer id) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public List<Issue> listIssues(Integer projectId) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public void updateIssue(Integer id, String title, String description) {

    }
    // TODO: Implements the method
    @Override
    public boolean deleteIssue(Integer id) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public void patchStatus(Integer id, Integer newStatusId) {

    }
    // TODO: Implements the method
    @Override
    public void patchAssignee(Integer id, Integer assigneeId) {

    }
    // TODO: Implements the method
    @Override
    public Integer createStatus(Integer projectId, String name, StatusCategory category) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public List<Status> getStatuses(Integer projectId) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public void updateStatus(Integer id, String name) {

    }
    // TODO: Implements the method
    @Override
    public boolean deleteStatus(Integer id) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public List<IssueHistory> getHistory(Integer issueId) {
        return null;
    }
    // TODO: Implements the method
    private void recordHistory(Integer issueId, String field, String oldVal, String newVal) {

    }
}
