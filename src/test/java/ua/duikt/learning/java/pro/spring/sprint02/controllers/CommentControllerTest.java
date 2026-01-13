package ua.duikt.learning.java.pro.spring.sprint02.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.CommentController;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.service.DetailsService;

import java.util.List;

import static org.mockito.BDDMockito.given;
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
    private DetailsService detailsService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Add Comment: Should return 201 Created")
    void addComment_Success() throws Exception {
        int issueId = 1;
        var request = new AddCommentRequest("This is a comment");

        given(detailsService.addComment(issueId, "This is a comment")).willReturn(true);

        mockMvc.perform(post("/api/issues/{issueId}/comments", issueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Comment added"));
    }

    @Test
    @DisplayName("Get Comments: Should return list")
    void getComments_Success() throws Exception {
        int issueId = 1;
        IssueComment comment = new IssueComment();
        comment.setContent("Test content");

        given(detailsService.getComments(issueId)).willReturn(List.of(comment));

        mockMvc.perform(get("/api/issues/{issueId}/comments", issueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].content").value("Test content"));
    }

    @Test
    @DisplayName("Update Comment: Should return 200 OK")
    void updateComment_Success() throws Exception {
        int commentId = 5;
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
        given(detailsService.deleteComment(5)).willReturn(true);

        mockMvc.perform(delete("/api/comments/{commentId}", 5))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete Comment: Should return 404 Not Found if missing")
    void deleteComment_NotFound() throws Exception {
        given(detailsService.deleteComment(99)).willReturn(false);

        mockMvc.perform(delete("/api/comments/{commentId}", 99))
                .andExpect(status().isNotFound());
    }
}
