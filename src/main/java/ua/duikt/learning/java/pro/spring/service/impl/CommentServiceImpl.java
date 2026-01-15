package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.repositories.CommentRepo;
import ua.duikt.learning.java.pro.spring.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;

    @Override
    @Transactional
    public boolean addComment(Long issueId, String content, Long userId) {
        IssueComment comment = IssueComment.builder()
                .issueId(issueId)
                .userId(userId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepo.save(comment);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueComment> getComments(Long issueId) {
        return commentRepo.findAllByIssueId(issueId);
    }

    @Override
    @Transactional
    public void updateComment(Long id, String content) {
        commentRepo.findById(id).ifPresent(c -> {
            c.setContent(content);
            c.setUpdatedAt(LocalDateTime.now());
        });
    }

    @Override
    @Transactional
    public boolean deleteComment(Long id) {
        if (commentRepo.existsById(id)) {
            commentRepo.deleteById(id);
            return true;
        }
        return false;
    }
}