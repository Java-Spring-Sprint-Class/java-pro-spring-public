package ua.duikt.learning.java.pro.spring.service.impl;

import ua.duikt.learning.java.pro.spring.entity.Project;
import ua.duikt.learning.java.pro.spring.entity.ProjectMember;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;
import ua.duikt.learning.java.pro.spring.service.ProjectService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
// TODO: Implements all necessary methods
public class ProjectServiceImpl implements ProjectService {
    // TODO: Implements the method
    @Override
    public Long createProject(String name, String key, String description, Long ownerId) {
        return 0L;
    }

    // TODO: Implements the method
    @Override
    public Project getProject(Long id) {
        return null;
    }

    // TODO: Implements the method
    @Override
    public List<Project> listProjects() {
        return List.of();
    }

    // TODO: Implements the method
    @Override
    public void updateProject(Long id, String name, String description) {

    }

    // TODO: Implements the method
    @Override
    public boolean deleteProject(Long id) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public boolean addMember(Long projectId, Long userId, ProjectRoleType role) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public List<ProjectMember> getMembers(Long projectId) {
        return List.of();
    }

    // TODO: Implements the method
    @Override
    public boolean removeMember(Long projectId, Long userId) {
        return false;
    }
}
