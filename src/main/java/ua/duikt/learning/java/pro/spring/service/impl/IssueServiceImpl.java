package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
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
@Service
public class IssueServiceImpl implements IssueService {
    private final Map<Long, Issue> issueTable = new ConcurrentHashMap<>();
    private final Map<Long, Status> statusTable = new ConcurrentHashMap<>();
    private final List<IssueHistory> historyTable = new ArrayList<>();

    private final AtomicLong issueIdGen = new AtomicLong(1);
    private final AtomicLong statusIdGen = new AtomicLong(1);
    private final AtomicLong historyIdGen = new AtomicLong(1);

    @Override
    public Long createIssue(Long projectId, String title, String description, IssueType type, Priority priority, Long statusId) {
        Long id = issueIdGen.getAndIncrement();
        Issue issue = Issue.builder()
                .id(id)
                .projectId(projectId)
                .title(title)
                .description(description)
                .type(type)
                .priority(priority)
                .statusId(statusId)
                .createdAt(LocalDateTime.now())
                .build();

        issueTable.put(id, issue);

        recordHistory(id, "creation", null, "created");

        return id;
    }

    @Override
    public Issue getIssue(Long id) {
        return issueTable.get(id);
    }

    @Override
    public List<Issue> listIssues(Long projectId) {
        return issueTable.values().stream()
                .filter(i -> i.getProjectId().equals(projectId))
                .toList();
    }

    @Override
    public void updateIssue(Long id, String title, String description) {
        Issue issue = issueTable.get(id);
        if (issue != null) {
            issue.setTitle(title);
            issue.setDescription(description);
            issue.setUpdatedAt(LocalDateTime.now());
            recordHistory(id, "details", "old", "updated");
        }
    }

    @Override
    public boolean deleteIssue(Long id) {
        return issueTable.remove(id) != null;
    }

    @Override
    public void patchStatus(Long id, Long newStatusId) {
        Issue issue = issueTable.get(id);
        if (issue != null) {
            String oldStatus = String.valueOf(issue.getStatusId());
            issue.setStatusId(newStatusId);
            recordHistory(id, "status", oldStatus, String.valueOf(newStatusId));
        }
    }

    @Override
    public void patchAssignee(Long id, Long assigneeId) {
        Issue issue = issueTable.get(id);
        if (issue != null) {
            String oldAssignee = String.valueOf(issue.getAssigneeId());
            issue.setAssigneeId(assigneeId);
            recordHistory(id, "assignee", oldAssignee, String.valueOf(assigneeId));
        }
    }

    @Override
    public Long createStatus(Long projectId, String name, StatusCategory category) {
        Long id = statusIdGen.getAndIncrement();

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
    public List<Status> getStatuses(Long projectId) {
        return statusTable.values().stream()
                .filter(s -> s.getProjectId().equals(projectId))
                .sorted(Comparator.comparingInt(Status::getPosition))
                .toList();
    }

    @Override
    public void updateStatus(Long id, String name) {
        Status s = statusTable.get(id);
        if (s != null) s.setName(name);
    }

    @Override
    public boolean deleteStatus(Long id) {
        return statusTable.remove(id) != null;
    }

    @Override
    public List<IssueHistory> getHistory(Long issueId) {
        return historyTable.stream()
                .filter(h -> h.getIssueId().equals(issueId))
                .toList();
    }


    private void recordHistory(Long issueId, String field, String oldVal, String newVal) {
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
