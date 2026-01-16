package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.entity.Label;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface DetailsService {
    // ===== Comments =====

    boolean addComment(Long issueId, String content, Long userid);

    List<IssueComment> getComments(Long issueId);

    void updateComment(Long id, String content);

    boolean deleteComment(Long id);

    // ===== Attachments =====

    boolean addAttachment(Long issueId, String fileName, String fileUrl, Integer fileSize, Long userid);

    List<Attachment> getAttachments(Long issueId);

    boolean deleteAttachment(Long id);

    // ===== Labels =====

    Long createLabel(String name, String color);

    List<Label> getLabels();

    boolean addLabelToIssue(Long issueId, Long labelId);

    boolean removeLabelFromIssue(Long issueId, Long labelId);

    List<Label> getLabelsForIssue(Long issueId);
}
