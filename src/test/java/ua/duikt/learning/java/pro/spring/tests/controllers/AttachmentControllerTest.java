package ua.duikt.learning.java.pro.spring.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.AttachmentController;
import ua.duikt.learning.java.pro.spring.dtos.AddAttachmentRequest;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.service.AttachmentService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@WebMvcTest(AttachmentController.class)
class AttachmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AttachmentService attachmentService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Add Attachment: Should return 201")
    void addAttachment_Success() throws Exception {
        Long issueId = 1L;
        Long userId = 2L;
        var request = new AddAttachmentRequest("file.png", "http://url.com", 1024);

        doNothing().when(attachmentService).addAttachment(issueId, "file.png", "http://url.com", 1024, userId);

        mockMvc.perform(post("/api/user/{userId}/issues/{issueId}/attachments", userId, issueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Attachment added"));
    }

    @Test
    @DisplayName("Delete Attachment: Should return 204")
    void deleteAttachment_Success() throws Exception {
        doNothing().when(attachmentService).deleteAttachment(10L);

        mockMvc.perform(delete("/api/attachments/{id}", 10L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete Attachment: Should return 404 if not found")
    void deleteAttachment_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Attachment not found"))
                .when(attachmentService).deleteAttachment(99L);

        mockMvc.perform(delete("/api/attachments/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Attachment not found"));
    }

    @Test
    @DisplayName("Get Attachments: Should return list")
    void getAttachments_Success() throws Exception {
        Attachment att = new Attachment();
        att.setFileName("doc.pdf");

        given(attachmentService.getAttachments(1L)).willReturn(List.of(att));

        mockMvc.perform(get("/api/issues/{issueId}/attachments", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileName").value("doc.pdf"));
    }

}
