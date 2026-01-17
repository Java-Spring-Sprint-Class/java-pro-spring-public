package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @PostMapping("/labels")
    public ResponseEntity<Map<String, Object>> createLabel(@RequestBody @Valid CreateLabelRequest request) {
        log.info("Request to create label: {} with color: {}", request.getName(), request.getColor());
        Long labelId = labelService.createLabel(request.getName(), request.getColor());
        log.info("Label created successfully with id: {}", labelId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", labelId, "message", "Label created"));
    }

    @GetMapping("/labels")
    public ResponseEntity<List<Label>> getAllLabels() {
        log.info("Request to get all labels");
        return ResponseEntity.ok(labelService.getLabels());
    }

    @PostMapping("/issues/{issueId}/labels/{labelId}")
    public ResponseEntity<String> addLabelToIssue(@PathVariable Long issueId,
                                                  @PathVariable Long labelId) {
        log.info("Request to add label id: {} to issue id: {}", labelId, issueId);
        labelService.addLabelToIssue(issueId, labelId);
        log.info("Label added to issue successfully");
        return ResponseEntity.ok("Label added to issue");
    }

    @GetMapping("/issues/{issueId}/labels")
    public ResponseEntity<List<Label>> getLabelsForIssue(@PathVariable Long issueId) {
        log.info("Request to get labels for issue id: {}", issueId);
        return ResponseEntity.ok(labelService.getLabelsForIssue(issueId));
    }

    @DeleteMapping("/issues/{issueId}/labels/{labelId}")
    public ResponseEntity<String> removeLabelFromIssue(@PathVariable Long issueId,
                                                       @PathVariable Long labelId) {
        log.info("Request to remove label id: {} from issue id: {}", labelId, issueId);
        labelService.removeLabelFromIssue(issueId, labelId);
        log.info("Label removed from issue successfully");
        return ResponseEntity.ok("Label removed from issue");
    }
}