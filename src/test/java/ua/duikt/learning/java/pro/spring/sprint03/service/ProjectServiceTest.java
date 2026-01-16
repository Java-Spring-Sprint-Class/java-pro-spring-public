package ua.duikt.learning.java.pro.spring.sprint03.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.Project;
import ua.duikt.learning.java.pro.spring.entity.ProjectMember;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;
import ua.duikt.learning.java.pro.spring.repositories.ProjectMemberRepo;
import ua.duikt.learning.java.pro.spring.repositories.ProjectRepo;
import ua.duikt.learning.java.pro.spring.service.impl.ProjectServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private ProjectMemberRepo projectMemberRepo;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    @DisplayName("createProject: should save project and return id")
    void createProject_shouldSaveProjectAndReturnId() {
        
        Project savedProject = Project.builder()
                .id(1L)
                .build();

        when(projectRepo.save(any(Project.class))).thenReturn(savedProject);

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);

        
        Long result = projectService.createProject(
                "Test Project",
                "TP",
                "Description",
                100L
        );

        
        assertThat(result).isEqualTo(1L);
        verify(projectRepo).save(captor.capture());

        Project project = captor.getValue();
        assertThat(project.getName()).isEqualTo("Test Project");
        assertThat(project.getKey()).isEqualTo("TP");
        assertThat(project.getOwnerId()).isEqualTo(100L);
        assertThat(project.getDescription()).isEqualTo("Description");
        assertThat(project.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("getProject: should return project when found")
    void getProject_shouldReturnProject() {
        
        Project project = Project.builder().id(1L).build();
        when(projectRepo.findById(1L)).thenReturn(Optional.of(project));

        
        Project result = projectService.getProject(1L);

        
        assertThat(result).isNotNull();
        verify(projectRepo).findById(1L);
    }

    @Test
    @DisplayName("getProject: should return null when not found")
    void getProject_shouldReturnNullIfNotFound() {
        
        when(projectRepo.findById(99L)).thenReturn(Optional.empty());

        
        Project result = projectService.getProject(99L);

        
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("listProjects: should return all projects")
    void listProjects_shouldReturnAllProjects() {
        
        when(projectRepo.findAll())
                .thenReturn(List.of(new Project(), new Project()));

        
        List<Project> result = projectService.listProjects();

        
        assertThat(result).hasSize(2);
        verify(projectRepo).findAll();
    }

    @Test
    @DisplayName("updateProject: should update fields when project exists")
    void updateProject_shouldUpdateProject() {
        
        Project project = Project.builder()
                .id(1L)
                .name("Old")
                .description("Old desc")
                .build();

        when(projectRepo.findById(1L)).thenReturn(Optional.of(project));

        
        projectService.updateProject(1L, "New", "New desc");

        
        assertThat(project.getName()).isEqualTo("New");
        assertThat(project.getDescription()).isEqualTo("New desc");
        assertThat(project.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("updateProject: should do nothing when project does not exist")
    void updateProject_shouldDoNothingIfNotExists() {
        
        when(projectRepo.findById(99L)).thenReturn(Optional.empty());

        
        projectService.updateProject(99L, "x", "y");

        
        verify(projectRepo).findById(99L);
    }

    @Test
    @DisplayName("deleteProject: should delete project and return true if exists")
    void deleteProject_shouldDeleteAndReturnTrue() {
        
        when(projectRepo.existsById(1L)).thenReturn(true);

        
        boolean result = projectService.deleteProject(1L);

        
        assertThat(result).isTrue();
        verify(projectRepo).deleteById(1L);
    }

    @Test
    @DisplayName("deleteProject: should return false if project does not exist")
    void deleteProject_shouldReturnFalseIfNotExists() {
        
        when(projectRepo.existsById(1L)).thenReturn(false);

        
        boolean result = projectService.deleteProject(1L);

        
        assertThat(result).isFalse();
        verify(projectRepo, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("addMember: should add member when not exists")
    void addMember_shouldSaveMember_ifNotExists() {
        
        Long projectId = 1L;
        Long userId = 10L;

        when(projectMemberRepo.existsByProjectIdAndUserId(projectId, userId))
                .thenReturn(false);

        ArgumentCaptor<ProjectMember> captor =
                ArgumentCaptor.forClass(ProjectMember.class);

        
        boolean result = projectService.addMember(
                projectId,
                userId,
                ProjectRoleType.DEVELOPER
        );

        
        assertThat(result).isTrue();
        verify(projectMemberRepo).save(captor.capture());

        ProjectMember member = captor.getValue();
        assertThat(member.getProjectId()).isEqualTo(projectId);
        assertThat(member.getUserId()).isEqualTo(userId);
        assertThat(member.getRole()).isEqualTo(ProjectRoleType.DEVELOPER);
    }

    @Test
    @DisplayName("addMember: should return false when member already exists")
    void addMember_shouldReturnFalse_ifExists() {
        
        when(projectMemberRepo.existsByProjectIdAndUserId(1L, 10L))
                .thenReturn(true);

        
        boolean result = projectService.addMember(
                1L,
                10L,
                ProjectRoleType.DEVELOPER
        );

        
        assertThat(result).isFalse();
        verify(projectMemberRepo, never()).save(any());
    }

    @Test
    @DisplayName("getMembers: should return project members")
    void getMembers_shouldReturnMembers() {
        
        when(projectMemberRepo.findAllByProjectId(1L))
                .thenReturn(List.of(new ProjectMember(), new ProjectMember()));

        
        List<ProjectMember> result = projectService.getMembers(1L);

        
        assertThat(result).hasSize(2);
        verify(projectMemberRepo).findAllByProjectId(1L);
    }

    @Test
    @DisplayName("removeMember: should delete member and return true if exists")
    void removeMember_shouldDeleteAndReturnTrue() {
        
        when(projectMemberRepo.existsByProjectIdAndUserId(1L, 10L))
                .thenReturn(true);

        
        boolean result = projectService.removeMember(1L, 10L);

        
        assertThat(result).isTrue();
        verify(projectMemberRepo).deleteByProjectIdAndUserId(1L, 10L);
    }

    @Test
    @DisplayName("removeMember: should return false if member does not exist")
    void removeMember_shouldReturnFalseIfNotExists() {
        
        when(projectMemberRepo.existsByProjectIdAndUserId(1L, 10L))
                .thenReturn(false);

        
        boolean result = projectService.removeMember(1L, 10L);

        
        assertThat(result).isFalse();
        verify(projectMemberRepo, never())
                .deleteByProjectIdAndUserId(anyLong(), anyLong());
    }
}
