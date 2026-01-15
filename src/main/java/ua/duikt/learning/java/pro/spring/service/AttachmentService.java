package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.Attachment;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface AttachmentService {
    boolean addAttachment(Long issueId, String fileName, String fileUrl, Integer fileSize);

    List<Attachment> getAttachments(Long issueId);

    boolean deleteAttachment(Long id);
}
