package ua.duikt.learning.java.pro.spring.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.StatusController;
import ua.duikt.learning.java.pro.spring.dtos.CreateStatusRequest;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;
import ua.duikt.learning.java.pro.spring.service.StatusService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@WebMvcTest(StatusController.class)
class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private StatusService statusService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create Status: should return 201")
    void createStatus_Success() throws Exception {
        var request = new CreateStatusRequest(
                1L, "To Do", StatusCategory.TO_DO
        );

        given(statusService.createStatus(1L, "To Do", StatusCategory.TO_DO))
                .willReturn(55L);

        mockMvc.perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55L));
    }

    @Test
    @DisplayName("Get Statuses: filters by projectId")
    void getStatuses_Success() throws Exception {
        Status s1 = new Status(); s1.setName("Done");
        given(statusService.getStatuses(10L)).willReturn(List.of(s1));

        mockMvc.perform(get("/api/statuses")
                        .param("projectId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Done"));
    }

    @Test
    @DisplayName("Delete Status: returns 204 if success")
    void deleteStatus_Success() throws Exception {
        given(statusService.deleteStatus(1L)).willReturn(true);

        mockMvc.perform(delete("/api/statuses/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete Status: returns 404 if failed")
    void deleteStatus_NotFound() throws Exception {
        given(statusService.deleteStatus(99L)).willReturn(false);

        mockMvc.perform(delete("/api/statuses/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}