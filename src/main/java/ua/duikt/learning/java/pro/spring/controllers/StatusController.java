package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.duikt.learning.java.pro.spring.dtos.CreateStatusRequest;
import ua.duikt.learning.java.pro.spring.dtos.UpdateStatusRequest;
import ua.duikt.learning.java.pro.spring.entity.Status;
import ua.duikt.learning.java.pro.spring.service.StatusService;

import java.util.List;
import java.util.Map;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createStatus(@RequestBody @Valid CreateStatusRequest request) {
        log.info("Request to create status: {} for project id: {}", request.getName(), request.getProjectId());
        Long statusId = statusService.createStatus(
                request.getProjectId(),
                request.getName(),
                request.getCategory()
        );

        log.info("Status created successfully with id: {}", statusId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", statusId, "message", "Status created"));
    }

    @GetMapping
    public ResponseEntity<List<Status>> getStatuses(@RequestParam Long projectId) {
        log.info("Request to get statuses for project id: {}", projectId);
        return ResponseEntity.ok(statusService.getStatuses(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable Long id,
                                               @RequestBody @Valid UpdateStatusRequest request) {
        log.info("Request to update status id: {}", id);
        statusService.updateStatus(id, request.getName());
        log.info("Status updated successfully");
        return ResponseEntity.ok("Status updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        log.info("Request to delete status id: {}", id);
        statusService.deleteStatus(id);
        log.info("Status deleted successfully");
        return ResponseEntity.noContent().build();
    }
}