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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    private final Map<Integer, Project> projectTable = new ConcurrentHashMap<>();
    private final List<ProjectMember> memberTable = new ArrayList<>();
    private final AtomicInteger projectIdGen = new AtomicInteger(1);
    private final AtomicInteger memberIdGen = new AtomicInteger(1);

    @Override
    public Integer createProject(String name, String key, String description) {
        Integer id = projectIdGen.getAndIncrement();
        Project project = Project.builder()
                .id(id)
                .name(name)
                .key(key)
                .ownerId(1)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();
        projectTable.put(id, project);
        return id;
    }

    @Override
    public Project getProject(Integer id) {
        return projectTable.get(id);
    }

    @Override
    public List<Project> listProjects() {
        return new ArrayList<>(projectTable.values());
    }

    public void updateProject(Integer id, String name, String description) {
        Project p = projectTable.get(id);
        if (p != null) {
            p.setName(name);
            p.setDescription(description);
            p.setUpdatedAt(LocalDateTime.now());
        }
    }

    public boolean deleteProject(Integer id) {
        return projectTable.remove(id) != null;
    }

    public boolean addMember(Integer projectId, Integer userId, ProjectRoleType role) {
        ProjectMember member = ProjectMember.builder()
                .id(memberIdGen.getAndIncrement())
                .projectId(projectId)
                .userId(userId)
                .role(role)
                .build();
        return memberTable.add(member);
    }

    @Override
    public List<ProjectMember> getMembers(Integer projectId) {
        return memberTable.stream()
                .filter(m -> m.getProjectId().equals(projectId))
                .toList();
    }

    @Override
    public boolean removeMember(Integer projectId, Integer userId) {
        return memberTable.removeIf(m ->
                m.getProjectId().equals(projectId) && m.getUserId().equals(userId));
    }
}
