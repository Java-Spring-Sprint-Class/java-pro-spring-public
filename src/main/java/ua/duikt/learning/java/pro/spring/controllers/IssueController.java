package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.duikt.learning.java.pro.spring.dtos.CreateIssueRequest;
import ua.duikt.learning.java.pro.spring.dtos.PatchAssigneeRequest;
import ua.duikt.learning.java.pro.spring.dtos.PatchStatusRequest;
import ua.duikt.learning.java.pro.spring.dtos.UpdateIssueRequest;
import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;
import ua.duikt.learning.java.pro.spring.service.IssueService;

import java.util.List;
import java.util.Map;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createIssue(@RequestBody @Valid CreateIssueRequest request) {
        log.info("Request to create issue in project id: {}, title: {}", request.getProjectId(), request.getTitle());
        Long issueId = issueService.createIssue(
                request.getProjectId(),
                request.getTitle(),
                request.getDescription(),
                request.getType(),
                request.getPriority(),
                request.getStatusId()
        );

        log.info("Issue created successfully with id: {}", issueId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", issueId, "message", "Issue created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssue(@PathVariable Long id) {
        log.info("Request to get issue by id: {}", id);
        return ResponseEntity.ok(issueService.getIssue(id));
    }

    @GetMapping
    public ResponseEntity<List<Issue>> listIssues(@RequestParam Long projectId) {
        log.info("Request to list issues for project id: {}", projectId);
        return ResponseEntity.ok(issueService.listIssues(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIssue(
            @PathVariable Long id,
            @RequestBody @Valid UpdateIssueRequest request) {

        log.info("Request to update issue id: {}", id);
        issueService.updateIssue(id, request.getTitle(), request.getDescription());
        log.info("Issue updated successfully: {}", id);
        return ResponseEntity.ok("Issue details updated");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        log.info("Request to delete issue id: {}", id);
        issueService.deleteIssue(id);
        log.info("Issue deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid PatchStatusRequest request) {

        log.info("Request to update status for issue id: {} to status id: {}", id, request.getStatusId());
        issueService.patchStatus(id, request.getStatusId());
        log.info("Status updated successfully for issue: {}", id);
        return ResponseEntity.ok("Status updated");
    }

    @PatchMapping("/{id}/assignee")
    public ResponseEntity<String> updateAssignee(
            @PathVariable Long id,
            @RequestBody @Valid PatchAssigneeRequest request) {

        log.info("Request to update assignee for issue id: {} to user id: {}", id, request.getAssigneeId());
        issueService.patchAssignee(id, request.getAssigneeId());
        log.info("Assignee updated successfully for issue: {}", id);
        return ResponseEntity.ok("Assignee updated");
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<IssueHistory>> getHistory(@PathVariable Long id) {
        log.info("Request to get history for issue id: {}", id);
        List<IssueHistory> history = issueService.getHistory(id);
        return ResponseEntity.ok(history);
    }
}