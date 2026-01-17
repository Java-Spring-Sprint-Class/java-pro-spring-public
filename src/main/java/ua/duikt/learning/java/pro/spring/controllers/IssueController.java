package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createIssue(@RequestBody @Valid CreateIssueRequest request) {
        Long issueId = issueService.createIssue(
                request.getProjectId(),
                request.getTitle(),
                request.getDescription(),
                request.getType(),
                request.getPriority(),
                request.getStatusId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", issueId, "message", "Issue created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssue(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.getIssue(id));
    }

    @GetMapping
    public ResponseEntity<List<Issue>> listIssues(@RequestParam Long projectId) {
        return ResponseEntity.ok(issueService.listIssues(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIssue(
            @PathVariable Long id,
            @RequestBody @Valid UpdateIssueRequest request) {

        issueService.updateIssue(id, request.getTitle(), request.getDescription());
        return ResponseEntity.ok("Issue details updated");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid PatchStatusRequest request) {

        issueService.patchStatus(id, request.getStatusId());
        return ResponseEntity.ok("Status updated");
    }

    @PatchMapping("/{id}/assignee")
    public ResponseEntity<String> updateAssignee(
            @PathVariable Long id,
            @RequestBody @Valid PatchAssigneeRequest request) {

        issueService.patchAssignee(id, request.getAssigneeId());
        return ResponseEntity.ok("Assignee updated");
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<IssueHistory>> getHistory(@PathVariable Long id) {
        List<IssueHistory> history = issueService.getHistory(id);
        return ResponseEntity.ok(history);
    }

}
