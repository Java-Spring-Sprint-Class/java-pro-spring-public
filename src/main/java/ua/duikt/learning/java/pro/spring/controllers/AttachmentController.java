package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.duikt.learning.java.pro.spring.dtos.AddAttachmentRequest;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.service.AttachmentService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/user/{userId}/issues/{issueId}/attachments")
    public ResponseEntity<String> addAttachment(
            @PathVariable Long issueId,
            @PathVariable Long userId,
            @RequestBody @Valid AddAttachmentRequest request) {

        log.info("Request to add attachment to issue id: {} by user id: {} (File: {})", issueId, userId, request.getFileName());
        attachmentService.addAttachment(issueId,
                request.getFileName(),
                request.getFileUrl(),
                request.getFileSize(),
                userId);

        log.info("Attachment added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body("Attachment added");
    }

    @GetMapping("/issues/{issueId}/attachments")
    public ResponseEntity<List<Attachment>> getAttachments(@PathVariable Long issueId) {
        log.info("Request to get attachments for issue id: {}", issueId);
        return ResponseEntity.ok(attachmentService.getAttachments(issueId));
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) {
        log.info("Request to delete attachment id: {}", attachmentId);
        attachmentService.deleteAttachment(attachmentId);
        log.info("Attachment deleted successfully");
        return ResponseEntity.noContent().build();
    }
}