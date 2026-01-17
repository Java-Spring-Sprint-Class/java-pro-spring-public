package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.duikt.learning.java.pro.spring.dtos.AddMemberRequest;
import ua.duikt.learning.java.pro.spring.dtos.CreateProjectRequest;
import ua.duikt.learning.java.pro.spring.dtos.UpdateProjectRequest;
import ua.duikt.learning.java.pro.spring.entity.Project;
import ua.duikt.learning.java.pro.spring.entity.ProjectMember;
import ua.duikt.learning.java.pro.spring.service.ProjectService;

import java.util.List;
import java.util.Map;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProject(@RequestBody @Valid CreateProjectRequest request) {
        log.info("Request to create project: {} (key: {}) by user id: {}", request.getName(), request.getKey(), request.getUserId());
        Long projectId = projectService.createProject(
                request.getName(),
                request.getKey(),
                request.getDescription(),
                request.getUserId()
        );

        log.info("Project created successfully with id: {}", projectId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", projectId, "message", "Project created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.info("Request to get project by id: {}", id);
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @GetMapping
    public ResponseEntity<List<Project>> listProjects() {
        log.info("Request to list all projects");
        return ResponseEntity.ok(projectService.listProjects());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProject(@PathVariable Long id,
                                                @RequestBody @Valid UpdateProjectRequest request) {
        log.info("Request to update project id: {}", id);
        projectService.updateProject(id, request.getName(), request.getDescription());
        log.info("Project updated successfully: {}", id);
        return ResponseEntity.ok("Project updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.info("Request to delete project id: {}", id);
        projectService.deleteProject(id);
        log.info("Project deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<String> addMember(@PathVariable Long projectId,
                                            @RequestBody @Valid AddMemberRequest request) {
        log.info("Request to add member user id: {} to project id: {} with role: {}", request.getUserId(), projectId, request.getRole());
        projectService.addMember(projectId, request.getUserId(), request.getRole());
        log.info("Member added successfully");
        return ResponseEntity.ok("Member added to project");
    }


    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMember>> getProjectMembers(@PathVariable Long projectId) {
        log.info("Request to get members for project id: {}", projectId);
        return ResponseEntity.ok(projectService.getMembers(projectId));
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<String> removeMember(@PathVariable Long projectId,
                                               @PathVariable Long userId) {
        log.info("Request to remove member user id: {} from project id: {}", userId, projectId);
        projectService.removeMember(projectId, userId);
        log.info("Member removed successfully");
        return ResponseEntity.ok("Member removed from project");
    }
}