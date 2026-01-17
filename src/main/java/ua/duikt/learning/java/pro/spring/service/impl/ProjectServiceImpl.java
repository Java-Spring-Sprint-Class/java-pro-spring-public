package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.Project;
import ua.duikt.learning.java.pro.spring.entity.ProjectMember;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;
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

        return projectRepo.save(project).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Project getProject(Long id) {
        return projectRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> listProjects() {
        return projectRepo.findAll();
    }

    @Override
    @Transactional
    public void updateProject(Long id, String name, String description) {
        projectRepo.findById(id).ifPresent(p -> {
            p.setName(name);
            p.setDescription(description);
            p.setUpdatedAt(LocalDateTime.now());
        });
    }

    @Override
    @Transactional
    public boolean deleteProject(Long id) {
        if (projectRepo.existsById(id)) {
            projectRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean addMember(Long projectId, Long userId, ProjectRoleType role) {
        if (projectMemberRepo.existsByProjectIdAndUserId(projectId, userId)) {
            return false;
        }

        ProjectMember member = ProjectMember.builder()
                .projectId(projectId)
                .userId(userId)
                .role(role)
                .build();

        projectMemberRepo.save(member);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectMember> getMembers(Long projectId) {
        return projectMemberRepo.findAllByProjectId(projectId);
    }

    @Override
    @Transactional
    public boolean removeMember(Long projectId, Long userId) {
        if (projectMemberRepo.existsByProjectIdAndUserId(projectId, userId)) {
            projectMemberRepo.deleteByProjectIdAndUserId(projectId, userId);
            return true;
        }
        return false;
    }
}