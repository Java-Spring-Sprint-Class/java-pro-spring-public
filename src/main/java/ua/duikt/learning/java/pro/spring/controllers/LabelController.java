package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.duikt.learning.java.pro.spring.dtos.CreateLabelRequest;
import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.service.LabelService;

import java.util.List;
import java.util.Map;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @PostMapping("/labels")
    public ResponseEntity<Map<String, Object>> createLabel(@RequestBody @Valid CreateLabelRequest request) {
        Long labelId = labelService.createLabel(request.getName(), request.getColor());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", labelId, "message", "Label created"));
    }

    @GetMapping("/labels")
    public ResponseEntity<List<Label>> getAllLabels() {
        return ResponseEntity.ok(labelService.getLabels());
    }

    @PostMapping("/issues/{issueId}/labels/{labelId}")
    public ResponseEntity<String> addLabelToIssue(@PathVariable Long issueId,
                                                  @PathVariable Long labelId) {
        boolean added = labelService.addLabelToIssue(issueId, labelId);
        if (added) {
            return ResponseEntity.ok("Label added to issue");
        }
        return ResponseEntity.badRequest().body("Failed to add label");
    }

    @GetMapping("/issues/{issueId}/labels")
    public ResponseEntity<List<Label>> getLabelsForIssue(@PathVariable Long issueId) {
        return ResponseEntity.ok(labelService.getLabelsForIssue(issueId));
    }

    @DeleteMapping("/issues/{issueId}/labels/{labelId}")
    public ResponseEntity<String> removeLabelFromIssue(@PathVariable Long issueId,
                                                       @PathVariable Long labelId) {
        boolean removed = labelService.removeLabelFromIssue(issueId, labelId);
        if (removed) {
            return ResponseEntity.ok("Label removed from issue");
        }
        return ResponseEntity.notFound().build();
    }
}
