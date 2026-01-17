package ua.duikt.learning.java.pro.spring.tests.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.entity.Issue;
import ua.duikt.learning.java.pro.spring.exceptions.BadRequestException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.repositories.AttachmentRepo;
import ua.duikt.learning.java.pro.spring.repositories.IssueRepo;
import ua.duikt.learning.java.pro.spring.service.impl.AttachmentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {

    @Mock
    private IssueRepo issueRepo;

    @Mock
    private AttachmentRepo attachmentRepo;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Test
    @DisplayName("addAttachment: should save attachment")
    void addAttachment_shouldSaveAttachment() {
        Long issueId = 1L;
        Long userId = 10L;
        when(issueRepo.findById(issueId)).thenReturn(Optional.of(new Issue()));

        attachmentService.addAttachment(issueId, "spec.pdf", "http://url", 1024, userId);

        ArgumentCaptor<Attachment> captor = ArgumentCaptor.forClass(Attachment.class);
        verify(attachmentRepo).save(captor.capture());

        Attachment saved = captor.getValue();
        assertThat(saved.getIssueId()).isEqualTo(issueId);
        assertThat(saved.getFileName()).isEqualTo("spec.pdf");
    }

    @Test
    @DisplayName("addAttachment: should throw BadRequestException if file data is invalid")
    void addAttachment_shouldThrowBadRequestException_ifInvalidData() {
        Long issueId = 1L;
        Long userId = 10L;

        assertThrows(BadRequestException.class, () -> attachmentService.addAttachment(issueId, "", null, 0, userId));

        verify(attachmentRepo, never()).save(any());
    }

    @Test
    @DisplayName("addAttachment: should throw ResourceNotFoundException if issue missing")
    void addAttachment_shouldThrowNotFound_ifIssueNotExists() {
        Long issueId = 99L;
        when(issueRepo.findById(issueId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                attachmentService.addAttachment(issueId, "file.txt", "url", 100, 1L)
        );

        verify(attachmentRepo, never()).save(any());
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
    @DisplayName("deleteAttachment: should delete attachment if it exists")
    void deleteAttachment_shouldDelete_ifExists() {
        Long attachmentId = 3L;
        Attachment attachment = Attachment.builder().id(attachmentId).build();

        when(attachmentRepo.findById(attachmentId)).thenReturn(Optional.of(attachment));

        attachmentService.deleteAttachment(attachmentId);

        verify(attachmentRepo).delete(attachment);
    }

    @Test
    @DisplayName("deleteAttachment: should throw ResourceNotFoundException if attachment does not exist")
    void deleteAttachment_shouldThrowResourceNotFoundException_ifNotExists() {
        Long attachmentId = 99L;
        when(attachmentRepo.findById(attachmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> attachmentService.deleteAttachment(attachmentId));

        verify(attachmentRepo, never()).delete(any());
    }
}
