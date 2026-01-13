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
    public Integer createProject(String name, String key, String description) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public Project getProject(Integer id) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public List<Project> listProjects() {
        return null;
    }
    // TODO: Implements the method
    @Override
    public void updateProject(Integer id, String name, String description) {

    }
    // TODO: Implements the method
    @Override
    public boolean deleteProject(Integer id) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public boolean addMember(Integer projectId, Integer userId, ProjectRoleType role) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public List<ProjectMember> getMembers(Integer projectId) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public boolean removeMember(Integer projectId, Integer userId) {
        return false;
    }
}
