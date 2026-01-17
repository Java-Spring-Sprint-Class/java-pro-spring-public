package ua.duikt.learning.java.pro.spring.tests.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.exceptions.BadRequestException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.repositories.CommentRepo;
import ua.duikt.learning.java.pro.spring.repositories.IssueRepo;
import ua.duikt.learning.java.pro.spring.repositories.UserRepo;
import ua.duikt.learning.java.pro.spring.service.impl.CommentServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private IssueRepo issueRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private CommentRepo commentRepo;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("addComment: should save comment")
    void addComment_shouldSaveComment() {
        Long issueId = 1L;
        Long userId = 5L;
        String content = "Test comment";

        when(issueRepo.existsById(issueId)).thenReturn(true);
        when(userRepo.existsById(userId)).thenReturn(true);

        commentService.addComment(issueId, content, userId);

        ArgumentCaptor<IssueComment> captor = ArgumentCaptor.forClass(IssueComment.class);
        verify(commentRepo).save(captor.capture());

        IssueComment saved = captor.getValue();
        assertThat(saved.getIssueId()).isEqualTo(issueId);
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getContent()).isEqualTo(content);
        assertThat(saved.getCreatedAt()).isNotNull();
    }
    @Test
    @DisplayName("addComment: should throw BadRequestException if content is empty")
    void addComment_shouldThrowBadRequestException_ifContentIsEmpty() {
        Long issueId = 1L;
        Long userId = 5L;
        String content = "";

        assertThrows(BadRequestException.class, () -> commentService.addComment(issueId, content, userId));

        verify(commentRepo, never()).save(any());
    }

    @Test
    @DisplayName("getComments: should return comments by issueId")
    void getComments_shouldReturnCommentsByIssueId() {
        Long issueId = 2L;
        List<IssueComment> comments = List.of(
                IssueComment.builder().id(1L).issueId(issueId).build(),
                IssueComment.builder().id(2L).issueId(issueId).build()
        );

        when(commentRepo.findAllByIssueId(issueId)).thenReturn(comments);

        List<IssueComment> result = commentService.getComments(issueId);

        assertThat(result).hasSize(2);
        verify(commentRepo).findAllByIssueId(issueId);
    }

    @Test
    @DisplayName("updateComment: should update content and updatedAt when comment exists")
    void updateComment_shouldUpdateContentAndUpdatedAt_ifExists() {
        Long commentId = 10L;
        IssueComment comment = IssueComment.builder()
                .id(commentId)
                .content("Old content")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        when(commentRepo.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.updateComment(commentId, "Updated content");

        assertThat(comment.getContent()).isEqualTo("Updated content");
        assertThat(comment.getUpdatedAt()).isNotNull();
        verify(commentRepo).findById(commentId);
    }

    @Test
    @DisplayName("updateComment: should throw ResourceNotFoundException when comment does not exist")
    void updateComment_shouldThrowException_ifNotExists() {
        Long commentId = 99L;
        when(commentRepo.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                commentService.updateComment(commentId, "New content")
        );

        verify(commentRepo).findById(commentId);
        verify(commentRepo, never()).save(any());
    }

    @Test
    @DisplayName("deleteComment: should delete comment if exists")
    void deleteComment_shouldDelete_ifExists() {
        Long commentId = 3L;
        IssueComment comment = IssueComment.builder().id(commentId).build();
        when(commentRepo.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.deleteComment(commentId);

        verify(commentRepo).delete(comment);
    }

    @Test
    @DisplayName("deleteComment: should throw ResourceNotFoundException if comment does not exist")
    void deleteComment_shouldThrowResourceNotFoundException_ifNotExists() {
        Long commentId = 100L;
        when(commentRepo.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(commentId));

        verify(commentRepo, never()).delete(any());
    }
}
