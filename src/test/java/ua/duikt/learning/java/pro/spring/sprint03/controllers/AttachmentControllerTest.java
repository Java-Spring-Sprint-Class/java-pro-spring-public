<<<<<<<< HEAD:src/test/java/ua/duikt/learning/java/pro/spring/sprint02/controllers/AttachmentControllerTest.java
package ua.duikt.learning.java.pro.spring.sprint02.controllers;
========
package ua.duikt.learning.java.pro.spring.sprint03.controllers;
>>>>>>>> refs/heads/task-sprint-3:src/test/java/ua/duikt/learning/java/pro/spring/sprint03/controllers/AttachmentControllerTest.java

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
import ua.duikt.learning.java.pro.spring.service.AttachmentService;

import java.util.List;

import static org.mockito.BDDMockito.given;
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
        int issueId = 1;
        var request = new AddAttachmentRequest("file.png", "http://url.com", 1024);

        given(attachmentService.addAttachment(issueId, "file.png", "http://url.com", 1024))
                .willReturn(true);

        mockMvc.perform(post("/api/issues/{issueId}/attachments", issueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Attachment added"));
    }

    @Test
    @DisplayName("Get Attachments: Should return list")
    void getAttachments_Success() throws Exception {
        Attachment att = new Attachment();
        att.setFileName("doc.pdf");

        given(attachmentService.getAttachments(1)).willReturn(List.of(att));

        mockMvc.perform(get("/api/issues/{issueId}/attachments", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileName").value("doc.pdf"));
    }

    @Test
    @DisplayName("Delete Attachment: Should return 204")
    void deleteAttachment_Success() throws Exception {
        given(attachmentService.deleteAttachment(10)).willReturn(true);

        mockMvc.perform(delete("/api/attachments/{id}", 10))
                .andExpect(status().isNoContent());
    }
}
