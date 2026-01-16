package ua.duikt.learning.java.pro.spring.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.LabelController;
import ua.duikt.learning.java.pro.spring.dtos.CreateLabelRequest;
import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.service.LabelService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@WebMvcTest(LabelController.class)
class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private LabelService labelService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create Label: Should return 201 and ID")
    void createLabel_Success() throws Exception {
        var request = new CreateLabelRequest("BUG", "#FF0000");
        given(labelService.createLabel("BUG", "#FF0000")).willReturn(55L);

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55L));
    }

    @Test
    @DisplayName("Get All Labels: Should return list")
    void getAllLabels_Success() throws Exception {
        Label label = new Label();
        label.setName("Feature");
        given(labelService.getLabels()).willReturn(List.of(label));

        mockMvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Feature"));
    }

    @Test
    @DisplayName("Add Label to Issue: Should return 200 OK")
    void addLabelToIssue_Success() throws Exception {
        Long issueId = 1L;
        Long labelId = 5L;
        given(labelService.addLabelToIssue(issueId, labelId)).willReturn(true);

        mockMvc.perform(post("/api/issues/{issueId}/labels/{labelId}", issueId, labelId))
                .andExpect(status().isOk())
                .andExpect(content().string("Label added to issue"));
    }

    @Test
    @DisplayName("Get Labels for Issue: Should return list")
    void getLabelsForIssue_Success() throws Exception {
        Label l = new Label(); l.setColor("blue");
        given(labelService.getLabelsForIssue(1L)).willReturn(List.of(l));

        mockMvc.perform(get("/api/issues/{issueId}/labels", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("blue"));
    }

    @Test
    @DisplayName("Remove Label from Issue: Should return 200 if removed")
    void removeLabelFromIssue_Success() throws Exception {
        given(labelService.removeLabelFromIssue(1L, 5L)).willReturn(true);

        mockMvc.perform(delete("/api/issues/{issueId}/labels/{labelId}", 1L, 5L))
                .andExpect(status().isOk())
                .andExpect(content().string("Label removed from issue"));
    }

    @Test
    @DisplayName("Remove Label from Issue: Should return 404 if not found")
    void removeLabelFromIssue_NotFound() throws Exception {
        given(labelService.removeLabelFromIssue(1L, 99L)).willReturn(false);

        mockMvc.perform(delete("/api/issues/{issueId}/labels/{labelId}", 1L, 99L))
                .andExpect(status().isNotFound());
    }
}
