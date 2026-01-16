package ua.duikt.learning.java.pro.spring.sprint01.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.service.DetailsService;
import ua.duikt.learning.java.pro.spring.service.impl.DetailsServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
class DetailsServiceTest {

    private DetailsService detailsService;

    @BeforeEach
    void setUp() {
        detailsService = new DetailsServiceImpl();
    }

    @Test
    @DisplayName("Comments Logic: Add, Get, Update, Delete")
    void commentsLogic() {
        detailsService.addComment(10L, "First comment", 1L);
        detailsService.addComment(10L, "Second comment", 1L);

        List<IssueComment> comments = detailsService.getComments(10L);
        assertThat(comments).hasSize(2);

        Long firstCommentId = comments.getFirst().getId();

        detailsService.updateComment(firstCommentId, "Updated content");

        List<IssueComment> updatedComments = detailsService.getComments(10L);
        IssueComment updatedComment = updatedComments.stream()
                .filter(c -> c.getId().equals(firstCommentId))
                .findFirst()
                .orElseThrow();

        assertThat(updatedComment.getContent()).isEqualTo("Updated content");

        boolean isDeleted = detailsService.deleteComment(firstCommentId);
        assertThat(isDeleted).isTrue();

        assertThat(detailsService.getComments(10L)).hasSize(1);
    }

    @Test
    @DisplayName("Attachments Logic: Add, Get, Delete")
    void attachmentsLogic() {
        detailsService.addAttachment(10L, "log.txt", "http://s3...", 1024, 1L);
        detailsService.addAttachment(10L, "image.png", "http://s3...", 2048, 1L);

        List<Attachment> attachments = detailsService.getAttachments(10L);
        assertThat(attachments).hasSize(2);
        assertThat(attachments.getFirst().getFileName()).isEqualTo("log.txt");

        Long attachmentId = attachments.getFirst().getId();

        boolean isDeleted = detailsService.deleteAttachment(attachmentId);
        assertThat(isDeleted).isTrue();

        assertThat(detailsService.getAttachments(10L)).hasSize(1);
    }

    @Test
    @DisplayName("Labels Logic: Create, Get All, Link, Unlink")
    void labelsLogic() {
        Long l1 = detailsService.createLabel("Backend", "Blue");
        Long l2 = detailsService.createLabel("Frontend", "Red");

        List<Label> allLabels = detailsService.getLabels();
        assertThat(allLabels).hasSize(2);

        detailsService.addLabelToIssue(100L, l1);
        detailsService.addLabelToIssue(100L, l2);

        List<Label> issueLabels = detailsService.getLabelsForIssue(100L);
        assertThat(issueLabels).hasSize(2);
        assertThat(issueLabels).extracting(Label::getName).contains("Backend", "Frontend");

        assertThat(detailsService.getLabelsForIssue(999L)).isEmpty();

        boolean isRemoved = detailsService.removeLabelFromIssue(100L, l1);
        assertThat(isRemoved).isTrue();

        List<Label> labelsAfterRemoval = detailsService.getLabelsForIssue(100L);
        assertThat(labelsAfterRemoval).hasSize(1);
        assertThat(labelsAfterRemoval.getFirst().getId()).isEqualTo(l2);
    }
}