package ua.duikt.learning.java.pro.spring.tests.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.repositories.AttachmentRepo;
import ua.duikt.learning.java.pro.spring.service.impl.AttachmentServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {

    @Mock
    private AttachmentRepo attachmentRepo;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Test
    @DisplayName("addAttachment: should save attachment and return true")
    void addAttachment_shouldSaveAttachmentAndReturnTrue() {
        Long issueId = 1L;
        Long userId = 10L;
        String fileName = "spec.pdf";
        String fileUrl = "http://files/spec.pdf";
        Integer fileSize = 1024;

        ArgumentCaptor<Attachment> captor = ArgumentCaptor.forClass(Attachment.class);

        boolean result = attachmentService.addAttachment(
                issueId, fileName, fileUrl, fileSize, userId
        );

        assertThat(result).isTrue();
        verify(attachmentRepo).save(captor.capture());

        Attachment saved = captor.getValue();
        assertThat(saved.getIssueId()).isEqualTo(issueId);
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getFileName()).isEqualTo(fileName);
        assertThat(saved.getFileUrl()).isEqualTo(fileUrl);
        assertThat(saved.getFileSize()).isEqualTo(fileSize);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("getAttachments: should return attachments by issueId")
    void getAttachments_shouldReturnAttachmentsByIssueId() {
        Long issueId = 5L;
        List<Attachment> attachments = List.of(
                Attachment.builder().id(1L).issueId(issueId).build(),
                Attachment.builder().id(2L).issueId(issueId).build()
        );

        when(attachmentRepo.findAllByIssueId(issueId)).thenReturn(attachments);

        List<Attachment> result = attachmentService.getAttachments(issueId);

        assertThat(result).hasSize(2);
        verify(attachmentRepo).findAllByIssueId(issueId);
    }

    @Test
    @DisplayName("deleteAttachment: should delete attachment if it exists and return true")
    void deleteAttachment_shouldDeleteAndReturnTrue_ifExists() {
        Long attachmentId = 3L;
        when(attachmentRepo.existsById(attachmentId)).thenReturn(true);

        boolean result = attachmentService.deleteAttachment(attachmentId);

        assertThat(result).isTrue();
        verify(attachmentRepo).deleteById(attachmentId);
    }

    @Test
    @DisplayName("deleteAttachment: should return false if attachment does not exist")
    void deleteAttachment_shouldReturnFalse_ifNotExists() {
        Long attachmentId = 99L;
        when(attachmentRepo.existsById(attachmentId)).thenReturn(false);

        boolean result = attachmentService.deleteAttachment(attachmentId);

        assertThat(result).isFalse();
        verify(attachmentRepo, never()).deleteById(anyLong());
    }
}
