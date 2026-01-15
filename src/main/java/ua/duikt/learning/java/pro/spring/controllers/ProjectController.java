package ua.duikt.learning.java.pro.spring.controllers;

import lombok.RequiredArgsConstructor;
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
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProject(@RequestBody CreateProjectRequest request) {
        Long projectId = projectService.createProject(
                request.getName(),
                request.getKey(),
                request.getDescription(),
                request.getUserId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", projectId, "message", "Project created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        Project project = projectService.getProject(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Project>> listProjects() {
        return ResponseEntity.ok(projectService.listProjects());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProject(@PathVariable Long id,
                                                @RequestBody UpdateProjectRequest request) {
        if (projectService.getProject(id) == null) {
            return ResponseEntity.notFound().build();
        }

        projectService.updateProject(id, request.getName(), request.getDescription());
        return ResponseEntity.ok("Project updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<String> addMember(@PathVariable Long projectId,
                                            @RequestBody AddMemberRequest request) {
        if (projectService.getProject(projectId) == null) {
            return ResponseEntity.notFound().build();
        }

        boolean added = projectService.addMember(projectId, request.getUserId(), request.getRole());

        if (added) {
            return ResponseEntity.ok("Member added to project");
        } else {
            return ResponseEntity.badRequest().body("Failed to add member");
        }
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMember>> getProjectMembers(@PathVariable Long projectId) {
        if (projectService.getProject(projectId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectService.getMembers(projectId));
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<String> removeMember(@PathVariable Long projectId,
                                               @PathVariable Long userId) {
        boolean removed = projectService.removeMember(projectId, userId);
        if (removed) {
            return ResponseEntity.ok("Member removed from project");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
