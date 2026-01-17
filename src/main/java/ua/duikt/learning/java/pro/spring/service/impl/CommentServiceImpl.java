package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
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
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final IssueRepo issueRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public void addComment(Long issueId, String content, Long userId) {
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException("Comment content cannot be empty");
        }
        if (!issueRepo.existsById(issueId)) {
            throw new ResourceNotFoundException("Issue with id " + issueId + " not found");
        }
        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }

        IssueComment comment = IssueComment.builder()
                .issueId(issueId)
                .userId(userId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepo.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueComment> getComments(Long issueId) {
        return commentRepo.findAllByIssueId(issueId);
    }

    @Override
    @Transactional
    public void updateComment(Long id, String content) {
        IssueComment comment = commentRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment with id " + id + " not found")
                );

        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        IssueComment comment = commentRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment with id " + id + " not found")
                );

        commentRepo.delete(comment);
    }

}