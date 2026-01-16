package ua.duikt.learning.java.pro.spring.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.repositories.CommentRepo;
import ua.duikt.learning.java.pro.spring.service.impl.CommentServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepo commentRepo;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("addComment: should save comment and return true")
    void addComment_shouldSaveCommentAndReturnTrue() {
        Long issueId = 1L;
        Long userId = 5L;
        String content = "Test comment";

        ArgumentCaptor<IssueComment> captor = ArgumentCaptor.forClass(IssueComment.class);

        boolean result = commentService.addComment(issueId, content, userId);

        assertThat(result).isTrue();
        verify(commentRepo).save(captor.capture());

        IssueComment saved = captor.getValue();
        assertThat(saved.getIssueId()).isEqualTo(issueId);
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getContent()).isEqualTo(content);
        assertThat(saved.getCreatedAt()).isNotNull();
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
    @DisplayName("updateComment: should do nothing when comment does not exist")
    void updateComment_shouldDoNothing_ifNotExists() {
        Long commentId = 99L;
        when(commentRepo.findById(commentId)).thenReturn(Optional.empty());

        commentService.updateComment(commentId, "New content");

        verify(commentRepo).findById(commentId);
        verify(commentRepo, never()).save(any());
    }

    @Test
    @DisplayName("deleteComment: should delete comment and return true if exists")
    void deleteComment_shouldDeleteAndReturnTrue_ifExists() {
        Long commentId = 3L;
        when(commentRepo.existsById(commentId)).thenReturn(true);

        boolean result = commentService.deleteComment(commentId);

        assertThat(result).isTrue();
        verify(commentRepo).deleteById(commentId);
    }

    @Test
    @DisplayName("deleteComment: should return false if comment does not exist")
    void deleteComment_shouldReturnFalse_ifNotExists() {
        Long commentId = 100L;
        when(commentRepo.existsById(commentId)).thenReturn(false);

        boolean result = commentService.deleteComment(commentId);

        assertThat(result).isFalse();
        verify(commentRepo, never()).deleteById(anyLong());
    }
}
