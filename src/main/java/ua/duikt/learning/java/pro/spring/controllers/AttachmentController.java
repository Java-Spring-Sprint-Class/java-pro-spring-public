package ua.duikt.learning.java.pro.spring.controllers;

import jakarta.validation.Valid;
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
    public ResponseEntity<String> addAttachment(
            @PathVariable Long issueId,
            @PathVariable Long userId,
            @RequestBody @Valid AddAttachmentRequest request) {

        attachmentService.addAttachment(issueId,
                request.getFileName(),
                request.getFileUrl(),
                request.getFileSize(),
                userId);

        return ResponseEntity.status(HttpStatus.CREATED).body("Attachment added");
    }

    @GetMapping("/issues/{issueId}/attachments")
    public ResponseEntity<List<Attachment>> getAttachments(@PathVariable Long issueId) {
        return ResponseEntity.ok(attachmentService.getAttachments(issueId));
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }

}
