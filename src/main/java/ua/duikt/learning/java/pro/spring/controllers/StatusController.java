package ua.duikt.learning.java.pro.spring.controllers;

import lombok.RequiredArgsConstructor;
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
@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createStatus(@RequestBody CreateStatusRequest request) {
        Integer statusId = statusService.createStatus(
                request.getProjectId(),
                request.getName(),
                request.getCategory()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", statusId, "message", "Status created"));
    }

    @GetMapping
    public ResponseEntity<List<Status>> getStatuses(@RequestParam Integer projectId) {
        return ResponseEntity.ok(statusService.getStatuses(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable Integer id,
                                               @RequestBody UpdateStatusRequest request) {
        statusService.updateStatus(id, request.getName());
        return ResponseEntity.ok("Status updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id) {
        boolean deleted = statusService.deleteStatus(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
