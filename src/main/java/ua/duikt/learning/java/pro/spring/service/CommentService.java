package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.IssueComment;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface CommentService {
    boolean addComment(Long issueId, String content, Long userId);

    List<IssueComment> getComments(Long issueId);

    void updateComment(Long id, String content);

    boolean deleteComment(Long id);
}
