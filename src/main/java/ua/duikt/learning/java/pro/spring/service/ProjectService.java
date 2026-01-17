package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.Project;
import ua.duikt.learning.java.pro.spring.entity.ProjectMember;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface ProjectService {
    Long createProject(String name, String key, String description, Long ownerId);

    Project getProject(Long id);

    List<Project> listProjects();

    void updateProject(Long id, String name, String description);

    void deleteProject(Long id);

    void addMember(Long projectId, Long userId, ProjectRoleType role);

    List<ProjectMember> getMembers(Long projectId);

    void removeMember(Long projectId, Long userId);
}
