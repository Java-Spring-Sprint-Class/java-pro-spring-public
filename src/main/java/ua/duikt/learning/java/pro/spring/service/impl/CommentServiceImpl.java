package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.exceptions.BadRequestException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.repositories.CommentRepo;
import ua.duikt.learning.java.pro.spring.repositories.IssueRepo;
import ua.duikt.learning.java.pro.spring.repositories.UserRepo;
import ua.duikt.learning.java.pro.spring.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final IssueRepo issueRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public void addComment(Long issueId, String content, Long userId) {
        log.info("Adding comment to issue ID: {} by user ID: {}", issueId, userId);
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException("Comment content cannot be empty");
        }
        if (!issueRepo.existsById(issueId)) {
            log.error("Issue ID: {} not found when adding comment", issueId);
            throw new ResourceNotFoundException("Issue with id " + issueId + " not found");
        }
        if (!userRepo.existsById(userId)) {
            log.error("User ID: {} not found when adding comment", userId);
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }

        IssueComment comment = IssueComment.builder()
                .issueId(issueId)
                .userId(userId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepo.save(comment);
        log.info("Comment added successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueComment> getComments(Long issueId) {
        log.info("Fetching comments for issue ID: {}", issueId);
        return commentRepo.findAllByIssueId(issueId);
    }

    @Override
    @Transactional
    public void updateComment(Long id, String content) {
        log.info("Updating comment ID: {}", id);
        IssueComment comment = commentRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment ID: {} not found for update", id);
                    return new ResourceNotFoundException("Comment with id " + id + " not found");
                });

        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        log.info("Comment ID: {} updated successfully", id);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        log.info("Deleting comment ID: {}", id);
        IssueComment comment = commentRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment ID: {} not found for deletion", id);
                    return new ResourceNotFoundException("Comment with id " + id + " not found");
                });

        commentRepo.delete(comment);
        log.info("Comment ID: {} deleted successfully", id);
    }
}