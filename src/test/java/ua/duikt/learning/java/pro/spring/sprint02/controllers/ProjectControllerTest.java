package ua.duikt.learning.java.pro.spring.sprint02.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.duikt.learning.java.pro.spring.controllers.ProjectController;
import ua.duikt.learning.java.pro.spring.dtos.AddMemberRequest;
import ua.duikt.learning.java.pro.spring.dtos.CreateProjectRequest;
import ua.duikt.learning.java.pro.spring.dtos.UpdateProjectRequest;
import ua.duikt.learning.java.pro.spring.entity.Project;
import ua.duikt.learning.java.pro.spring.entity.ProjectMember;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;
import ua.duikt.learning.java.pro.spring.service.ProjectService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("1. Create Project: Should return 201 and new ID")
    void createProject_Success() throws Exception {
        Long userId = 1L;
        var request = new CreateProjectRequest("New Project", "NP", "Desc", userId);

        given(projectService.createProject("New Project", "NP", "Desc", userId)).willReturn(55L);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55L))
                .andExpect(jsonPath("$.message").value("Project created successfully"));
    }

    @Test
    @DisplayName("2. Get Project: Should return Project JSON if found")
    void getProject_Success() throws Exception {
        Project project = new Project();
        project.setId(1L);
        project.setName("Alpha");

        given(projectService.getProject(1L)).willReturn(project);

        mockMvc.perform(get("/api/projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alpha"));
    }

    @Test
    @DisplayName("2. Get Project: Should return 404 if not found")
    void getProject_NotFound() throws Exception {
        given(projectService.getProject(99L)).willReturn(null);

        mockMvc.perform(get("/api/projects/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("4. Update Project: Should check existence, update and return 200")
    void updateProject_Success() throws Exception {
        Long projectId = 1L;
        var request = new UpdateProjectRequest("Updated Name", "Updated Desc");

        given(projectService.getProject(projectId)).willReturn(new Project());

        mockMvc.perform(put("/api/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Project updated"));

        verify(projectService).updateProject(projectId, "Updated Name", "Updated Desc");
    }

    @Test
    @DisplayName("4. Update Project: Should return 404 if project missing")
    void updateProject_NotFound() throws Exception {
        given(projectService.getProject(99L)).willReturn(null);
        var request = new UpdateProjectRequest("Val", "Desc");

        mockMvc.perform(put("/api/projects/{id}", 99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("5. Delete Project: Should return 204 No Content")
    void deleteProject_Success() throws Exception {
        given(projectService.deleteProject(1L)).willReturn(true);

        mockMvc.perform(delete("/api/projects/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("6. Add Member: Should return 200 when project exists")
    void addMember_Success() throws Exception {
        Long projectId = 1L;
        Long userId = 10L;
        var request = new AddMemberRequest(userId, ProjectRoleType.ADMIN);

        given(projectService.getProject(projectId)).willReturn(new Project());

        given(projectService.addMember(projectId, userId, ProjectRoleType.ADMIN)).willReturn(true);

        mockMvc.perform(post("/api/projects/{projectId}/members", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Member added to project"));
    }

    @Test
    @DisplayName("6. Add Member: Should return 404 if project missing")
    void addMember_ProjectNotFound() throws Exception {
        given(projectService.getProject(99L)).willReturn(null);
        var request = new AddMemberRequest(10L, ProjectRoleType.ADMIN);

        mockMvc.perform(post("/api/projects/{projectId}/members", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("7. Get Members: Should return list")
    void getProjectMembers_Success() throws Exception {
        Long projectId = 1L;
        ProjectMember member = new ProjectMember();

        given(projectService.getProject(projectId)).willReturn(new Project());
        given(projectService.getMembers(projectId)).willReturn(List.of(member));

        mockMvc.perform(get("/api/projects/{projectId}/members", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("8. Remove Member: Should return 200")
    void removeMember_Success() throws Exception {
        given(projectService.removeMember(1L, 10L)).willReturn(true);

        mockMvc.perform(delete("/api/projects/{projectId}/members/{userId}", 1L, 10L))
                .andExpect(status().isOk())
                .andExpect(content().string("Member removed from project"));
    }
}
