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
        detailsService.addComment(10, "First comment");
        detailsService.addComment(10, "Second comment");

        List<IssueComment> comments = detailsService.getComments(10);
        assertThat(comments).hasSize(2);

        Integer firstCommentId = comments.get(0).getId();

        detailsService.updateComment(firstCommentId, "Updated content");

        List<IssueComment> updatedComments = detailsService.getComments(10);
        IssueComment updatedComment = updatedComments.stream()
                .filter(c -> c.getId().equals(firstCommentId))
                .findFirst()
                .orElseThrow();

        assertThat(updatedComment.getContent()).isEqualTo("Updated content");

        boolean isDeleted = detailsService.deleteComment(firstCommentId);
        assertThat(isDeleted).isTrue();

        assertThat(detailsService.getComments(10)).hasSize(1);
    }

    @Test
    @DisplayName("Attachments Logic: Add, Get, Delete")
    void attachmentsLogic() {
        detailsService.addAttachment(10, "log.txt", "http://s3...", 1024);
        detailsService.addAttachment(10, "image.png", "http://s3...", 2048);

        List<Attachment> attachments = detailsService.getAttachments(10);
        assertThat(attachments).hasSize(2);
        assertThat(attachments.get(0).getFileName()).isEqualTo("log.txt");

        Integer attachmentId = attachments.get(0).getId();

        boolean isDeleted = detailsService.deleteAttachment(attachmentId);
        assertThat(isDeleted).isTrue();

        assertThat(detailsService.getAttachments(10)).hasSize(1);
    }

    @Test
    @DisplayName("Labels Logic: Create, Get All, Link, Unlink")
    void labelsLogic() {
        Integer l1 = detailsService.createLabel("Backend", "Blue");
        Integer l2 = detailsService.createLabel("Frontend", "Red");

        List<Label> allLabels = detailsService.getLabels();
        assertThat(allLabels).hasSize(2);

        detailsService.addLabelToIssue(100, l1);
        detailsService.addLabelToIssue(100, l2);

        List<Label> issueLabels = detailsService.getLabelsForIssue(100);
        assertThat(issueLabels).hasSize(2);
        assertThat(issueLabels).extracting(Label::getName).contains("Backend", "Frontend");

        assertThat(detailsService.getLabelsForIssue(999)).isEmpty();

        boolean isRemoved = detailsService.removeLabelFromIssue(100, l1);
        assertThat(isRemoved).isTrue();

        List<Label> labelsAfterRemoval = detailsService.getLabelsForIssue(100);
        assertThat(labelsAfterRemoval).hasSize(1);
        assertThat(labelsAfterRemoval.get(0).getId()).isEqualTo(l2);
    }
}