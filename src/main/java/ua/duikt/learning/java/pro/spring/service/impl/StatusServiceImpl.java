package ua.duikt.learning.java.pro.spring.service.impl;

import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;
import ua.duikt.learning.java.pro.spring.service.StatusService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
// TODO: Implements all necessary methods
public class StatusServiceImpl implements StatusService {
    // TODO: Implements the method
    @Override
    public Long createStatus(Long projectId, String name, StatusCategory category) {
        return 0L;
    }

    // TODO: Implements the method
    @Override
    public List<Status> getStatuses(Long projectId) {
        return List.of();
    }

    // TODO: Implements the method
    @Override
    public void updateStatus(Long id, String name) {

    }

    // TODO: Implements the method
    @Override
    public boolean deleteStatus(Long id) {
        return false;
    }
}
