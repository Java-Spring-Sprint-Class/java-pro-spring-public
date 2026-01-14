package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;
import ua.duikt.learning.java.pro.spring.service.StatusService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class StatusServiceImpl implements StatusService {
    private final Map<Integer, Status> statusTable = new ConcurrentHashMap<>();
    private final AtomicInteger statusIdGen = new AtomicInteger(1);

    @Override
    public Integer createStatus(Integer projectId, String name, StatusCategory category) {
        Integer id = statusIdGen.getAndIncrement();

        int nextPosition = statusTable.values().stream()
                                   .filter(s -> s.getProjectId().equals(projectId))
                                   .map(Status::getPosition)
                                   .filter(Objects::nonNull)
                                   .max(Integer::compareTo)
                                   .orElse(0) + 1;

        Status status = Status.builder()
                .id(id)
                .projectId(projectId)
                .name(name)
                .category(category)
                .position(nextPosition)
                .build();

        statusTable.put(id, status);
        return id;
    }


    @Override
    public List<Status> getStatuses(Integer projectId) {
        return statusTable.values().stream()
                .filter(s -> s.getProjectId().equals(projectId))
                .sorted(Comparator.comparingInt(Status::getPosition))
                .toList();
    }

    @Override
    public void updateStatus(Integer id, String name) {
        Status s = statusTable.get(id);
        if (s != null) s.setName(name);
    }

    @Override
    public boolean deleteStatus(Integer id) {
        return statusTable.remove(id) != null;
    }
}

