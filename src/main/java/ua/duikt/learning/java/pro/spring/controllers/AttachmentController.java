package ua.duikt.learning.java.pro.spring.controllers;

import lombok.RequiredArgsConstructor;
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
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/user/{userId}/issues/{issueId}/attachments")
    public ResponseEntity<String> addAttachment(@PathVariable Long issueId,
                                                @PathVariable Long userId,
                                                @RequestBody AddAttachmentRequest request) {
        boolean added = attachmentService.addAttachment(
                issueId,
                request.getFileName(),
                request.getFileUrl(),
                request.getFileSize(),
                userId
        );

        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Attachment added");
        }
        return ResponseEntity.badRequest().body("Failed to add attachment");
    }

    @GetMapping("/issues/{issueId}/attachments")
    public ResponseEntity<List<Attachment>> getAttachments(@PathVariable Long issueId) {
        return ResponseEntity.ok(attachmentService.getAttachments(issueId));
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) {
        boolean deleted = attachmentService.deleteAttachment(attachmentId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
