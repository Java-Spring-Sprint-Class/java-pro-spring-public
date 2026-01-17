package ua.duikt.learning.java.pro.spring.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.CommentController;
import ua.duikt.learning.java.pro.spring.dtos.AddCommentRequest;
import ua.duikt.learning.java.pro.spring.dtos.UpdateCommentRequest;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.exceptions.BadRequestException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.service.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CommentService commentService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Add Comment: Should return 201 Created")
    void addComment_Success() throws Exception {
        Long issueId = 1L;
        Long userId = 2L;
        var request = new AddCommentRequest("This is a comment");

        doNothing().when(commentService).addComment(issueId, "This is a comment", userId);

        mockMvc.perform(post("/api/user/{userId}/issues/{issueId}/comments", userId, issueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Comment added"));
    }

    @Test
    @DisplayName("Add Comment: Should return 400 Bad Request if content is empty")
    void addComment_BadRequest() throws Exception {
        Long issueId = 1L;
        Long userId = 2L;
        String emptyContent = "";
        var request = new AddCommentRequest(emptyContent);

        doThrow(new BadRequestException("Comment content cannot be empty"))
                .when(commentService).addComment(eq(issueId), eq(emptyContent), eq(userId));

        mockMvc.perform(post("/api/user/{userId}/issues/{issueId}/comments", userId, issueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Comment content cannot be empty"));
    }

    @Test
    @DisplayName("Get Comments: Should return list")
    void getComments_Success() throws Exception {
        Long issueId = 1L;
        IssueComment comment = new IssueComment();
        comment.setContent("Test content");

        given(commentService.getComments(issueId)).willReturn(List.of(comment));

        mockMvc.perform(get("/api/issues/{issueId}/comments", issueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].content").value("Test content"));
    }

    @Test
    @DisplayName("Update Comment: Should return 200 OK")
    void updateComment_Success() throws Exception {
        Long commentId = 5L;
        var request = new UpdateCommentRequest("Updated text");

        mockMvc.perform(put("/api/comments/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment updated"));
    }

    @Test
    @DisplayName("Delete Comment: Should return 204 No Content")
    void deleteComment_Success() throws Exception {
        doNothing().when(commentService).deleteComment(5L);

        mockMvc.perform(delete("/api/comments/{commentId}", 5L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete Comment: Should return 404 Not Found if missing")
    void deleteComment_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Comment not found"))
                .when(commentService).deleteComment(99L);

        mockMvc.perform(delete("/api/comments/{commentId}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Comment not found"));
    }
}
