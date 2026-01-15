package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.repositories.AttachmentRepo;
import ua.duikt.learning.java.pro.spring.service.AttachmentService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepo attachmentRepo;

    @Override
    @Transactional
    public boolean addAttachment(Long issueId, String fileName, String fileUrl, Integer fileSize, Long userId) {
        Attachment attachment = Attachment.builder()
                .issueId(issueId)
                .userId(userId)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileSize(fileSize)
                .createdAt(LocalDateTime.now())
                .build();

        attachmentRepo.save(attachment);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> getAttachments(Long issueId) {
        return attachmentRepo.findAllByIssueId(issueId);
    }

    @Override
    @Transactional
    public boolean deleteAttachment(Long id) {
        if (attachmentRepo.existsById(id)) {
            attachmentRepo.deleteById(id);
            return true;
        }
        return false;
    }
}