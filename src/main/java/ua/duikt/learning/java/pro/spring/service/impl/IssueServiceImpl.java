package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;
import ua.duikt.learning.java.pro.spring.service.IssueService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class IssueServiceImpl implements IssueService {
    private final Map<Integer, Issue> issueTable = new ConcurrentHashMap<>();
    private final Map<Integer, Status> statusTable = new ConcurrentHashMap<>();
    private final List<IssueHistory> historyTable = new ArrayList<>();

    private final AtomicInteger issueIdGen = new AtomicInteger(1);
    private final AtomicInteger statusIdGen = new AtomicInteger(1);
    private final AtomicInteger historyIdGen = new AtomicInteger(1);

    @Override
    public Integer createIssue(Integer projectId, String title, String description, IssueType type, Priority priority) {
        Integer id = issueIdGen.getAndIncrement();
        Issue issue = Issue.builder()
                .id(id)
                .projectId(projectId)
                .title(title)
                .description(description)
                .type(type)
                .priority(priority)
                .statusId(1)
                .createdAt(LocalDateTime.now())
                .build();

        issueTable.put(id, issue);

        recordHistory(id, "creation", null, "created");

        return id;
    }

    @Override
    public Issue getIssue(Integer id) {
        return issueTable.get(id);
    }

    @Override
    public List<Issue> listIssues(Integer projectId) {
        return issueTable.values().stream()
                .filter(i -> i.getProjectId().equals(projectId))
                .toList();
    }

    @Override
    public void updateIssue(Integer id, String title, String description) {
        Issue issue = issueTable.get(id);
        if (issue != null) {
            issue.setTitle(title);
            issue.setDescription(description);
            issue.setUpdatedAt(LocalDateTime.now());
            recordHistory(id, "details", "old", "updated");
        }
    }

    public boolean deleteIssue(Integer id) {
        return issueTable.remove(id) != null;
    }

    @Override
    public void patchStatus(Integer id, Integer newStatusId) {
        Issue issue = issueTable.get(id);
        if (issue != null) {
            String oldStatus = String.valueOf(issue.getStatusId());
            issue.setStatusId(newStatusId);
            recordHistory(id, "status", oldStatus, String.valueOf(newStatusId));
        }
    }

    @Override
    public void patchAssignee(Integer id, Integer assigneeId) {
        Issue issue = issueTable.get(id);
        if (issue != null) {
            String oldAssignee = String.valueOf(issue.getAssigneeId());
            issue.setAssigneeId(assigneeId);
            recordHistory(id, "assignee", oldAssignee, String.valueOf(assigneeId));
        }
    }

    @Override
    public Integer createStatus(Integer projectId, String name, StatusCategory category) {
        Integer id = statusIdGen.getAndIncrement();

        int nextPosition = statusTable.values().stream()
                                   .filter(s -> s.getProjectId().equals(projectId))
                                   .map(Status::getPosition)
                                   .filter(Objects::nonNull)
                                   .max(Integer::compareTo)
                                   .orElse(0) + 1;

        Status status = Status.builder()
                .id(id)
                .projectId(projectId)
                .name(name)
                .category(category)
                .position(nextPosition)
                .build();

        statusTable.put(id, status);
        return id;
    }


    @Override
    public List<Status> getStatuses(Integer projectId) {
        return statusTable.values().stream()
                .filter(s -> s.getProjectId().equals(projectId))
                .sorted(Comparator.comparingInt(Status::getPosition))
                .toList();
    }

    @Override
    public void updateStatus(Integer id, String name) {
        Status s = statusTable.get(id);
        if (s != null) s.setName(name);
    }

    @Override
    public boolean deleteStatus(Integer id) {
        return statusTable.remove(id) != null;
    }

    @Override
    public List<IssueHistory> getHistory(Integer issueId) {
        return historyTable.stream()
                .filter(h -> h.getIssueId().equals(issueId))
                .toList();
    }


    private void recordHistory(Integer issueId, String field, String oldVal, String newVal) {
        IssueHistory history = IssueHistory.builder()
                .id(historyIdGen.getAndIncrement())
                .issueId(issueId)
                .fieldChanged(field)
                .oldValue(oldVal)
                .newValue(newVal)
                .createdAt(LocalDateTime.now())
                .build();
        historyTable.add(history);
    }

}
