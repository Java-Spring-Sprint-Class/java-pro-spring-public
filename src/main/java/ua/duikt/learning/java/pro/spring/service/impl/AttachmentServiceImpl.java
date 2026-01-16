package ua.duikt.learning.java.pro.spring.service.impl;

import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.service.AttachmentService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
// TODO: Implements all necessary methods
public class AttachmentServiceImpl implements AttachmentService {
    // TODO: Implements the method
    @Override
    public boolean addAttachment(Long issueId, String fileName, String fileUrl, Long fileSize) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public List<Attachment> getAttachments(Long issueId) {
        return List.of();
    }
    // TODO: Implements the method
    @Override
    public boolean deleteAttachment(Long id) {
        return false;
    }
}
