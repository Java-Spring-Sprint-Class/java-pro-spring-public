package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.service.CommentService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
// TODO: Implements all necessary methods
public class CommentServiceImpl implements CommentService {
    // TODO: Implements the method
    @Override
    public boolean addComment(Long issueId, String content) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public List<IssueComment> getComments(Long issueId) {
        return List.of();
    }

    // TODO: Implements the method
    @Override
    public void updateComment(Long id, String content) {

    }

    // TODO: Implements the method
    @Override
    public boolean deleteComment(Long id) {
        return false;
    }
}
