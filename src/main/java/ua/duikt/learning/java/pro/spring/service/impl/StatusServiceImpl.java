package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepo statusRepository;
    private final ProjectRepo projectRepo;

    @Override
    @Transactional
    public Long createStatus(Long projectId, String name, StatusCategory category) {
        log.info("Creating status '{}' for project ID: {}", name, projectId);
        if (!projectRepo.existsById(projectId)) {
            log.error("Project with ID {} not found while creating status", projectId);
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

        Long id = statusRepository.save(status).getId();
        log.info("Status created successfully with ID: {}", id);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Status> getStatuses(Long projectId) {
        log.info("Fetching statuses for project ID: {}", projectId);
        return statusRepository.findAllByProjectIdOrderByPositionAsc(projectId);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String name) {
        log.info("Updating status ID: {} with new name: {}", id, name);
        statusRepository.findById(id).ifPresent(status -> status.setName(name));
    }

    @Override
    @Transactional
    public void deleteStatus(Long id) {
        log.info("Deleting status ID: {}", id);
        if (!statusRepository.existsById(id)) {
            log.error("Status ID: {} not found for deletion", id);
            throw new ResourceNotFoundException("Status not found");
        }
        statusRepository.deleteById(id);
        log.info("Status ID: {} deleted successfully", id);
    }
}