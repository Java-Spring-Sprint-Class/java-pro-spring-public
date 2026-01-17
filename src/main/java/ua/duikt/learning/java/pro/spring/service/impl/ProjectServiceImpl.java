package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.Project;
import ua.duikt.learning.java.pro.spring.entity.ProjectMember;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;
import ua.duikt.learning.java.pro.spring.exceptions.ConflictException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.repositories.ProjectMemberRepo;
import ua.duikt.learning.java.pro.spring.repositories.ProjectRepo;
import ua.duikt.learning.java.pro.spring.service.ProjectService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMemberRepo projectMemberRepo;

    @Override
    @Transactional
    public Long createProject(String name, String key, String description, Long ownerId) {
        log.info("Creating project '{}' with key '{}', owner: {}", name, key, ownerId);

        Project project = Project.builder()
                .name(name)
                .key(key)
                .ownerId(ownerId)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            Long id = projectRepo.save(project).getId();
            log.info("Project created successfully with ID: {}", id);
            return id;
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to create project. Key '{}' already exists", key);
            throw new ConflictException(
                    "Project with key '" + key + "' already exists"
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Project getProject(Long id) {
        log.info("Fetching project ID: {}", id);
        return projectRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Project ID: {} not found", id);
                    return new ResourceNotFoundException("Project not found id" + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> listProjects() {
        log.info("Listing all projects");
        return projectRepo.findAll();
    }

    @Override
    @Transactional
    public void updateProject(Long id, String name, String description) {
        log.info("Updating project ID: {}", id);

        Project project = projectRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Project ID: {} not found for update", id);
                    return new ResourceNotFoundException("Project not found id=" + id);
                });

        project.setName(name);
        project.setDescription(description);
        project.setUpdatedAt(LocalDateTime.now());
        projectRepo.save(project); // Added save for clarity, though transactional handles dirty checking
        log.info("Project updated successfully");
    }



    @Override
    @Transactional
    public void deleteProject(Long id) {
        log.info("Deleting project ID: {}", id);
        if (!projectRepo.existsById(id)) {
            log.error("Project ID: {} not found for deletion", id);
            throw new ResourceNotFoundException("Project not found id" + id);
        }
        projectRepo.deleteById(id);
        log.info("Project deleted successfully");
    }

    @Override
    @Transactional
    public void addMember(Long projectId, Long userId, ProjectRoleType role) {
        log.info("Adding member UserID: {} to ProjectID: {} with role: {}", userId, projectId, role);

        projectRepo.findById(projectId)
                .orElseThrow(() -> {
                    log.error("Project ID: {} not found when adding member", projectId);
                    return new ResourceNotFoundException("Project with id " + projectId + " not found");
                });

        if (projectMemberRepo.existsByProjectIdAndUserId(projectId, userId)) {
            log.warn("User ID: {} is already a member of Project ID: {}", userId, projectId);
            throw new ConflictException("User already a member of this project");
        }

        ProjectMember member = ProjectMember.builder()
                .projectId(projectId)
                .userId(userId)
                .role(role)
                .build();

        projectMemberRepo.save(member);
        log.info("Member added successfully");
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProjectMember> getMembers(Long projectId) {
        log.info("Fetching members for project ID: {}", projectId);
        return projectMemberRepo.findAllByProjectId(projectId);
    }

    @Override
    @Transactional
    public void removeMember(Long projectId, Long userId) {
        log.info("Removing member UserID: {} from ProjectID: {}", userId, projectId);
        if (!projectMemberRepo.existsByProjectIdAndUserId(projectId, userId)) {
            log.error("Member assignment not found. ProjectID: {}, UserID: {}", projectId, userId);
            throw new ResourceNotFoundException("Project with id " + projectId + " not found");
        }
        projectMemberRepo.deleteByProjectIdAndUserId(projectId, userId);
        log.info("Member removed successfully");
    }
}