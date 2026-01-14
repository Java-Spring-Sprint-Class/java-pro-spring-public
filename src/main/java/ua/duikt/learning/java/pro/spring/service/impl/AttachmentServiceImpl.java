package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.service.AttachmentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {
    private final Map<Integer, Attachment> attachmentTable = new ConcurrentHashMap<>();
    private final AtomicInteger attachmentIdGen = new AtomicInteger(1);

    public boolean addAttachment(Integer issueId, String fileName, String fileUrl, Integer fileSize) {
        Integer id = attachmentIdGen.getAndIncrement();
        Attachment attachment = Attachment.builder()
                .id(id)
                .issueId(issueId)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileSize(fileSize)
                .createdAt(LocalDateTime.now())
                .build();
        attachmentTable.put(id, attachment);
        return true;
    }

    public List<Attachment> getAttachments(Integer issueId) {
        return attachmentTable.values().stream()
                .filter(a -> a.getIssueId().equals(issueId))
                .toList();
    }

    public boolean deleteAttachment(Integer id) {
        return attachmentTable.remove(id) != null;
    }
}
