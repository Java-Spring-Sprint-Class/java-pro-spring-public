package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.repositories.ProjectRepo;
import ua.duikt.learning.java.pro.spring.repositories.StatusRepo;
import ua.duikt.learning.java.pro.spring.service.StatusService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepo statusRepository;
    private final ProjectRepo projectRepo;

    @Override
    @Transactional
    public Long createStatus(Long projectId, String name, StatusCategory category) {
        if (!projectRepo.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found");
        }

        Integer maxPosition = statusRepository.findMaxPositionByProjectId(projectId);

        int nextPosition = (maxPosition == null ? 0 : maxPosition) + 1;

        Status status = Status.builder()
                .projectId(projectId)
                .name(name)
                .category(category)
                .position(nextPosition)
                .build();

        return statusRepository.save(status).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Status> getStatuses(Long projectId) {
        return statusRepository.findAllByProjectIdOrderByPositionAsc(projectId);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String name) {
        statusRepository.findById(id).ifPresent(status -> status.setName(name));
    }

    @Override
    @Transactional
    public void deleteStatus(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new ResourceNotFoundException("Status not found");
        }
        statusRepository.deleteById(id);
    }
}