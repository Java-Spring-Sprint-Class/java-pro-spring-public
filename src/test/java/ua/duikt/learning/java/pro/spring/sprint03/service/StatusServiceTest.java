package ua.duikt.learning.java.pro.spring.sprint03.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;
import ua.duikt.learning.java.pro.spring.service.StatusService;
import ua.duikt.learning.java.pro.spring.service.impl.StatusServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
class StatusServiceTest {

    private StatusService statusService;

    @BeforeEach
    void setUp() {
        statusService = new StatusServiceImpl();
    }

    @Test
    @DisplayName("Status Management")
    void statusManagement() {
        statusService.createStatus(1, "To Do", StatusCategory.TO_DO);
        statusService.createStatus(1, "In Progress", StatusCategory.IN_PROGRESS);

        List<Status> statuses = statusService.getStatuses(1);
        assertThat(statuses).hasSize(2);
        assertThat(statuses.getFirst().getName()).isEqualTo("To Do");
    }

    @Test
    @DisplayName("Status Lifecycle: Create, Update, Delete")
    void statusLifecycle() {
        Integer sId = statusService.createStatus(1, "To Do", StatusCategory.TO_DO);

        List<Status> statuses = statusService.getStatuses(1);
        assertThat(statuses).hasSize(1);
        assertThat(statuses.getFirst().getName()).isEqualTo("To Do");

        statusService.updateStatus(sId, "To Do Updated");

        Status updatedStatus = statusService.getStatuses(1).getFirst();
        assertThat(updatedStatus.getName()).isEqualTo("To Do Updated");

        boolean deleted = statusService.deleteStatus(sId);
        assertThat(deleted).isTrue();
        assertThat(statusService.getStatuses(1)).isEmpty();
    }

}
