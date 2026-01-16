package ua.duikt.learning.java.pro.spring.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;
import ua.duikt.learning.java.pro.spring.repositories.StatusRepo;
import ua.duikt.learning.java.pro.spring.service.impl.StatusServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    private StatusRepo statusRepository;

    @InjectMocks
    private StatusServiceImpl statusService;

    @Test
    @DisplayName("createStatus: should create status with position = 1 when no statuses exist")
    void createStatus_shouldSetPositionOne_whenNoStatusesExist() {
        
        Long projectId = 1L;
        when(statusRepository.findMaxPositionByProjectId(projectId))
                .thenReturn(null);

        Status savedStatus = Status.builder().id(10L).build();
        when(statusRepository.save(any(Status.class))).thenReturn(savedStatus);

        ArgumentCaptor<Status> captor = ArgumentCaptor.forClass(Status.class);

        
        Long result = statusService.createStatus(
                projectId,
                "To Do",
                StatusCategory.TO_DO
        );

        
        assertThat(result).isEqualTo(10L);
        verify(statusRepository).save(captor.capture());

        Status status = captor.getValue();
        assertThat(status.getProjectId()).isEqualTo(projectId);
        assertThat(status.getName()).isEqualTo("To Do");
        assertThat(status.getCategory()).isEqualTo(StatusCategory.TO_DO);
        assertThat(status.getPosition()).isEqualTo(1);
    }

    @Test
    @DisplayName("createStatus: should increment position based on maxPosition")
    void createStatus_shouldIncrementPosition() {
        
        Long projectId = 1L;
        when(statusRepository.findMaxPositionByProjectId(projectId))
                .thenReturn(3);

        Status savedStatus = Status.builder().id(11L).build();
        when(statusRepository.save(any(Status.class))).thenReturn(savedStatus);

        ArgumentCaptor<Status> captor = ArgumentCaptor.forClass(Status.class);

        
        statusService.createStatus(
                projectId,
                "In Progress",
                StatusCategory.IN_PROGRESS
        );

        
        verify(statusRepository).save(captor.capture());

        Status status = captor.getValue();
        assertThat(status.getPosition()).isEqualTo(4);
    }

    @Test
    @DisplayName("getStatuses: should return statuses ordered by position")
    void getStatuses_shouldReturnStatusesOrderedByPosition() {
        
        Long projectId = 2L;
        when(statusRepository.findAllByProjectIdOrderByPositionAsc(projectId))
                .thenReturn(List.of(
                        Status.builder().position(1).build(),
                        Status.builder().position(2).build()
                ));

        
        List<Status> result = statusService.getStatuses(projectId);

        
        assertThat(result).hasSize(2);
        verify(statusRepository)
                .findAllByProjectIdOrderByPositionAsc(projectId);
    }

    @Test
    @DisplayName("updateStatus: should update status name when status exists")
    void updateStatus_shouldUpdateName_ifExists() {
        
        Status status = Status.builder()
                .id(5L)
                .name("Old")
                .build();

        when(statusRepository.findById(5L))
                .thenReturn(Optional.of(status));

        
        statusService.updateStatus(5L, "New");

        
        assertThat(status.getName()).isEqualTo("New");
        verify(statusRepository).findById(5L);
    }

    @Test
    @DisplayName("updateStatus: should do nothing when status does not exist")
    void updateStatus_shouldDoNothing_ifNotExists() {
        
        when(statusRepository.findById(99L))
                .thenReturn(Optional.empty());

        
        statusService.updateStatus(99L, "X");

        
        verify(statusRepository).findById(99L);
    }

    @Test
    @DisplayName("deleteStatus: should delete status and return true if exists")
    void deleteStatus_shouldDeleteAndReturnTrue() {
        
        when(statusRepository.existsById(1L)).thenReturn(true);

        
        boolean result = statusService.deleteStatus(1L);

        
        assertThat(result).isTrue();
        verify(statusRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteStatus: should return false if status does not exist")
    void deleteStatus_shouldReturnFalse_ifNotExists() {
        
        when(statusRepository.existsById(1L)).thenReturn(false);

        
        boolean result = statusService.deleteStatus(1L);

        
        assertThat(result).isFalse();
        verify(statusRepository, never()).deleteById(anyLong());
    }
}
