package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface StatusService {
    Long createStatus(Long projectId, String name, StatusCategory category);

    List<Status> getStatuses(Long projectId);

    void updateStatus(Long id, String name);

    boolean deleteStatus(Long id);
}
