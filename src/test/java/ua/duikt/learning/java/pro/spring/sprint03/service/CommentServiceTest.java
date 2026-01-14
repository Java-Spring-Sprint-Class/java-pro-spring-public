package ua.duikt.learning.java.pro.spring.sprint03.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.service.CommentService;
import ua.duikt.learning.java.pro.spring.service.impl.CommentServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
class CommentServiceTest {

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceImpl();
    }

    @Test
    @DisplayName("Comments Logic: Add, Get, Update, Delete")
    void commentsLogic() {
        commentService.addComment(10, "First comment");
        commentService.addComment(10, "Second comment");

        List<IssueComment> comments = commentService.getComments(10);
        assertThat(comments).hasSize(2);

        Integer firstCommentId = comments.getFirst().getId();

        commentService.updateComment(firstCommentId, "Updated content");

        List<IssueComment> updatedComments = commentService.getComments(10);
        IssueComment updatedComment = updatedComments.stream()
                .filter(c -> c.getId().equals(firstCommentId))
                .findFirst()
                .orElseThrow();

        assertThat(updatedComment.getContent()).isEqualTo("Updated content");

        boolean isDeleted = commentService.deleteComment(firstCommentId);
        assertThat(isDeleted).isTrue();

        assertThat(commentService.getComments(10)).hasSize(1);
    }
}
