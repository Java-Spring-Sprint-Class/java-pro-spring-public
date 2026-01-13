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
    Integer createProject(String name, String key, String description);

    Project getProject(Integer id);

    List<Project> listProjects();

    void updateProject(Integer id, String name, String description);

    boolean deleteProject(Integer id);

    boolean addMember(Integer projectId, Integer userId, ProjectRoleType role);

    List<ProjectMember> getMembers(Integer projectId);

    boolean removeMember(Integer projectId, Integer userId);
}
