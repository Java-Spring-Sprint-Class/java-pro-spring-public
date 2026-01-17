package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.exceptions.BadRequestException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.repositories.AttachmentRepo;
import ua.duikt.learning.java.pro.spring.repositories.IssueRepo;
import ua.duikt.learning.java.pro.spring.service.AttachmentService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepo attachmentRepo;
    private final IssueRepo issueRepo;

    @Override
    @Transactional
    public void addAttachment(Long issueId, String fileName, String fileUrl, Integer fileSize, Long userId) {
        log.info("Adding attachment to issue ID: {}. File: {}, User: {}", issueId, fileName, userId);
        if (fileName == null || fileName.isEmpty() || fileUrl == null) {
            throw new BadRequestException("Invalid attachment data");
        }

        issueRepo.findById(issueId)
                .orElseThrow(() -> {
                    log.error("Issue ID: {} not found when adding attachment", issueId);
                    return new ResourceNotFoundException("Issue not found id=" + issueId);
                });

        Attachment attachment = Attachment.builder()
                .issueId(issueId)
                .userId(userId)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileSize(fileSize)
                .createdAt(LocalDateTime.now())
                .build();

        attachmentRepo.save(attachment);
        log.info("Attachment added successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> getAttachments(Long issueId) {
        log.info("Fetching attachments for issue ID: {}", issueId);
        return attachmentRepo.findAllByIssueId(issueId);
    }

    @Override
    @Transactional
    public void deleteAttachment(Long id) {
        log.info("Deleting attachment ID: {}", id);
        Attachment attachment = attachmentRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Attachment ID: {} not found", id);
                    return new ResourceNotFoundException("Attachment not found id=" + id);
                });
        attachmentRepo.delete(attachment);
        log.info("Attachment deleted successfully");
    }

}