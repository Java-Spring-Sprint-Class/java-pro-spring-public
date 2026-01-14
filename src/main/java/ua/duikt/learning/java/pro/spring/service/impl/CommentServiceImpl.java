package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class CommentServiceImpl implements CommentService {
    private final Map<Integer, IssueComment> commentTable = new ConcurrentHashMap<>();
    private final AtomicInteger commentIdGen = new AtomicInteger(1);

    public boolean addComment(Integer issueId, String content) {
        Integer id = commentIdGen.getAndIncrement();
        IssueComment comment = IssueComment.builder()
                .id(id)
                .issueId(issueId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        commentTable.put(id, comment);
        return true;
    }

    public List<IssueComment> getComments(Integer issueId) {
        return commentTable.values().stream()
                .filter(c -> c.getIssueId().equals(issueId))
                .toList();
    }

    public void updateComment(Integer id, String content) {
        IssueComment c = commentTable.get(id);
        if (c != null) {
            c.setContent(content);
            c.setUpdatedAt(LocalDateTime.now());
        }
    }

    public boolean deleteComment(Integer id) {
        return commentTable.remove(id) != null;
    }
}
