package ua.duikt.learning.java.pro.spring.sprint03.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.IssueController;
import ua.duikt.learning.java.pro.spring.dtos.CreateIssueRequest;
import ua.duikt.learning.java.pro.spring.dtos.PatchStatusRequest;
import ua.duikt.learning.java.pro.spring.dtos.UpdateIssueRequest;
import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;
import ua.duikt.learning.java.pro.spring.service.IssueService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@WebMvcTest(IssueController.class)
class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private IssueService issueService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create Issue: returns 201 and ID")
    void createIssue_Success() throws Exception {
        Long statusId = 1L;
        var request = new CreateIssueRequest(
                1L, "Fix Bug", "Desc", IssueType.BUG, Priority.HIGH, statusId
        );

        given(issueService.createIssue(1L, "Fix Bug", "Desc", IssueType.BUG, Priority.HIGH, statusId))
                .willReturn(101L);

        mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101));
    }

    @Test
    @DisplayName("Get Issue: returns Issue if found")
    void getIssue_Success() throws Exception {
        Issue issue = new Issue();
        issue.setId(10L);
        issue.setTitle("Task 1");

        given(issueService.getIssue(10L)).willReturn(issue);

        mockMvc.perform(get("/api/issues/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    @DisplayName("List Issues: filters by projectId")
    void listIssues_Success() throws Exception {
        Issue issue = new Issue();
        given(issueService.listIssues(5L)).willReturn(List.of(issue));

        mockMvc.perform(get("/api/issues").param("projectId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("Update Issue: checks existence then updates")
    void updateIssue_Success() throws Exception {
        Long id = 1L;
        var request = new UpdateIssueRequest("New Title", "New Desc");

        given(issueService.getIssue(id)).willReturn(new Issue());

        mockMvc.perform(put("/api/issues/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Issue details updated"));
    }

    @Test
    @DisplayName("Update Issue: returns 404 if missing")
    void updateIssue_NotFound() throws Exception {
        given(issueService.getIssue(99L)).willReturn(null);
        var request = new UpdateIssueRequest("T", "D");

        mockMvc.perform(put("/api/issues/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Patch Status: should update status")
    void updateStatus_Success() throws Exception {
        Long issueId = 1L;
        Long newStatusId = 5L;
        var request = new PatchStatusRequest(newStatusId);

        given(issueService.getIssue(issueId)).willReturn(new Issue());

        mockMvc.perform(patch("/api/issues/{id}/status", issueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Status updated"));
    }

    @Test
    @DisplayName("Get History: returns list of changes")
    void getHistory_Success() throws Exception {
        Long issueId = 1L;
        given(issueService.getIssue(issueId)).willReturn(new Issue());
        given(issueService.getHistory(issueId)).willReturn(List.of(new IssueHistory()));

        mockMvc.perform(get("/api/issues/{id}/history", issueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}