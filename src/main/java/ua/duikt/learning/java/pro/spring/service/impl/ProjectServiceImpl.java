package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
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
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMemberRepo projectMemberRepo;

    @Override
    @Transactional
    public Long createProject(String name, String key, String description, Long ownerId) {

        Project project = Project.builder()
                .name(name)
                .projectKey(key)
                .ownerId(ownerId)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            return projectRepo.save(project).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    "Project with key '" + key + "' already exists"
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Project getProject(Long id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found id" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> listProjects() {
        return projectRepo.findAll();
    }

    @Override
    @Transactional
    public void updateProject(Long id, String name, String description) {

        Project project = projectRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found id=" + id)
                );

        project.setName(name);
        project.setDescription(description);
        project.setUpdatedAt(LocalDateTime.now());
    }



    @Override
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepo.existsById(id)) {
            throw new ResourceNotFoundException("Project not found id" + id);
        }
        projectRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void addMember(Long projectId, Long userId, ProjectRoleType role) {

        projectRepo.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project with id " + projectId + " not found")
                );

        if (projectMemberRepo.existsByProjectIdAndUserId(projectId, userId)) {
            throw new ConflictException("User already a member of this project");
        }

        ProjectMember member = ProjectMember.builder()
                .projectId(projectId)
                .userId(userId)
                .role(role)
                .build();

        projectMemberRepo.save(member);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProjectMember> getMembers(Long projectId) {
        return projectMemberRepo.findAllByProjectId(projectId);
    }

    @Override
    @Transactional
    public void removeMember(Long projectId, Long userId) {
        if (!projectMemberRepo.existsByProjectIdAndUserId(projectId, userId)) {
            throw new ResourceNotFoundException("Project with id " + projectId + " not found");
        }
        projectMemberRepo.deleteByProjectIdAndUserId(projectId, userId);
    }
}