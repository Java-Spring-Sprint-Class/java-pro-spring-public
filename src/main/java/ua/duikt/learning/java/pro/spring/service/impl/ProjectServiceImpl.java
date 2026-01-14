package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.Project;
import ua.duikt.learning.java.pro.spring.entity.ProjectMember;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;
import ua.duikt.learning.java.pro.spring.service.ProjectService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    private final Map<Long, Project> projectTable = new ConcurrentHashMap<>();
    private final List<ProjectMember> memberTable = new ArrayList<>();
    private final AtomicLong projectIdGen = new AtomicLong(1);
    private final AtomicLong memberIdGen = new AtomicLong(1);

    @Override
    public Long createProject(String name, String key, String description) {
        Long id = projectIdGen.getAndIncrement();
        Project project = Project.builder()
                .id(id)
                .name(name)
                .key(key)
                .ownerId(1L)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();
        projectTable.put(id, project);
        return id;
    }

    @Override
    public Project getProject(Long id) {
        return projectTable.get(id);
    }

    @Override
    public List<Project> listProjects() {
        return new ArrayList<>(projectTable.values());
    }

    @Override
    public void updateProject(Long id, String name, String description) {
        Project p = projectTable.get(id);
        if (p != null) {
            p.setName(name);
            p.setDescription(description);
            p.setUpdatedAt(LocalDateTime.now());
        }
    }

    @Override
    public boolean deleteProject(Long id) {
        return projectTable.remove(id) != null;
    }

    @Override
    public boolean addMember(Long projectId, Long userId, ProjectRoleType role) {
        ProjectMember member = ProjectMember.builder()
                .id(memberIdGen.getAndIncrement())
                .projectId(projectId)
                .userId(userId)
                .role(role)
                .build();
        return memberTable.add(member);
    }

    @Override
    public List<ProjectMember> getMembers(Long projectId) {
        return memberTable.stream()
                .filter(m -> m.getProjectId().equals(projectId))
                .toList();
    }

    @Override
    public boolean removeMember(Long projectId, Long userId) {
        return memberTable.removeIf(m ->
                m.getProjectId().equals(projectId) && m.getUserId().equals(userId));
    }
}
