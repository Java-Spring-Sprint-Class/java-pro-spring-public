package ua.duikt.learning.java.pro.spring.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.duikt.learning.java.pro.spring.dtos.AddAttachmentRequest;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.service.DetailsService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttachmentController {

    private final DetailsService detailsService;

    @PostMapping("/issues/{issueId}/attachments")
    public ResponseEntity<String> addAttachment(@PathVariable Integer issueId,
                                                @RequestBody AddAttachmentRequest request) {
        boolean added = detailsService.addAttachment(
                issueId,
                request.getFileName(),
                request.getFileUrl(),
                request.getFileSize()
        );

        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Attachment added");
        }
        return ResponseEntity.badRequest().body("Failed to add attachment");
    }

    @GetMapping("/issues/{issueId}/attachments")
    public ResponseEntity<List<Attachment>> getAttachments(@PathVariable Integer issueId) {
        return ResponseEntity.ok(detailsService.getAttachments(issueId));
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Integer attachmentId) {
        boolean deleted = detailsService.deleteAttachment(attachmentId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
