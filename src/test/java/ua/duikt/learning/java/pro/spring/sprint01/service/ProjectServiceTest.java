package ua.duikt.learning.java.pro.spring.sprint01.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.Project;
import ua.duikt.learning.java.pro.spring.entity.ProjectMember;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;
import ua.duikt.learning.java.pro.spring.service.ProjectService;
import ua.duikt.learning.java.pro.spring.service.impl.ProjectServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
class ProjectServiceTest {

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl();
    }

    @Test
    @DisplayName("Project CRUD Operations")
    void projectCrud() {
        Long userId1 = 101L;
        Long pId = projectService.createProject("Alpha", "ALP", "Desc", userId1);

        Project p = projectService.getProject(pId);
        assertThat(p.getName()).isEqualTo("Alpha");

        projectService.updateProject(pId, "Omega", "New Desc");
        assertThat(projectService.getProject(pId).getName()).isEqualTo("Omega");

        projectService.deleteProject(pId);
        assertThat(projectService.getProject(pId)).isNull();
    }

    @Test
    @DisplayName("Create and Get Project")
    void createAndGet() {
        Long userId1 = 101L;
        Long pId = projectService.createProject("Alpha", "ALP", "Description for Alpha", userId1);

        Project p = projectService.getProject(pId);
        assertThat(p).isNotNull();
        assertThat(p.getName()).isEqualTo("Alpha");
        assertThat(p.getProjectKey()).isEqualTo("ALP");
        assertThat(p.getId()).isEqualTo(pId);
    }

    @Test
    @DisplayName("List Projects")
    void listProjects() {
        Long userId1 = 101L;
        projectService.createProject("Project A", "PA", "Desc A", userId1);
        projectService.createProject("Project B", "PB", "Desc B",  userId1);

        List<Project> allProjects = projectService.listProjects();

        assertThat(allProjects).hasSize(2);
        assertThat(allProjects).extracting(Project::getProjectKey).contains("PA", "PB");
    }

    @Test
    @DisplayName("Update Project")
    void updateProject() {
        Long userId1 = 101L;
        Long pId = projectService.createProject("Old Name", "KEY", "Old Desc", userId1);

        projectService.updateProject(pId, "New Name", "New Desc");

        Project updated = projectService.getProject(pId);
        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getDescription()).isEqualTo("New Desc");
        assertThat(updated.getProjectKey()).isEqualTo("KEY");
    }

    @Test
    @DisplayName("Delete Project")
    void deleteProject() {
        Long userId1 = 101L;
        Long pId = projectService.createProject("To Delete", "DEL", "Desc", userId1);

        boolean isDeleted = projectService.deleteProject(pId);
        assertThat(isDeleted).isTrue();
        assertThat(projectService.getProject(pId)).isNull();

        boolean deleteAgain = projectService.deleteProject(pId);
        assertThat(deleteAgain).isFalse();
    }

    @Test
    @DisplayName("Manage Project Members: Add, Get, Remove")
    void memberManagement() {
        Long userId = 100L;
        Long userId1 = 101L;
        Long userId2 = 102L;
        Long pId = projectService.createProject("Team Project", "TEAM", "Desc", userId);

        boolean added1 = projectService.addMember(pId, userId1, ProjectRoleType.OWNER);
        boolean added2 = projectService.addMember(pId, userId2, ProjectRoleType.MEMBER);

        assertThat(added1).isTrue();
        assertThat(added2).isTrue();

        List<ProjectMember> members = projectService.getMembers(pId);
        assertThat(members).hasSize(2);

        assertThat(members).anyMatch(m -> m.getUserId().equals(userId1) && m.getRole() == ProjectRoleType.OWNER);
        assertThat(members).anyMatch(m -> m.getUserId().equals(userId2) && m.getRole() == ProjectRoleType.MEMBER);

        boolean removed = projectService.removeMember(pId, userId1);
        assertThat(removed).isTrue();

        assertThat(projectService.getMembers(pId)).hasSize(1);

        boolean removeFail = projectService.removeMember(pId, 999L);
        assertThat(removeFail).isFalse();
    }
}