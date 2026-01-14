package ua.duikt.learning.java.pro.spring.sprint03.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.service.AttachmentService;
import ua.duikt.learning.java.pro.spring.service.impl.AttachmentServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
class AttachmentServiceTest {
    private AttachmentService attachmentService;

    @BeforeEach
    void setUp() {
        attachmentService = new AttachmentServiceImpl();
    }

    @Test
    @DisplayName("Attachments Logic: Add, Get, Delete")
    void attachmentsLogic() {
        attachmentService.addAttachment(10, "log.txt", "http://s3...", 1024);
        attachmentService.addAttachment(10, "image.png", "http://s3...", 2048);

        List<Attachment> attachments = attachmentService.getAttachments(10);
        assertThat(attachments).hasSize(2);
        assertThat(attachments.getFirst().getFileName()).isEqualTo("log.txt");

        Integer attachmentId = attachments.getFirst().getId();

        boolean isDeleted = attachmentService.deleteAttachment(attachmentId);
        assertThat(isDeleted).isTrue();

        assertThat(attachmentService.getAttachments(10)).hasSize(1);
    }
}
