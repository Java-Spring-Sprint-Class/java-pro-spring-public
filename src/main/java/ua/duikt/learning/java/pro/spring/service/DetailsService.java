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

    boolean addComment(Integer issueId, String content);

    List<IssueComment> getComments(Integer issueId);

    void updateComment(Integer id, String content);

    boolean deleteComment(Integer id);

    // ===== Attachments =====

    boolean addAttachment(Integer issueId, String fileName, String fileUrl, Integer fileSize);

    List<Attachment> getAttachments(Integer issueId);

    boolean deleteAttachment(Integer id);

    // ===== Labels =====

    Integer createLabel(String name, String color);

    List<Label> getLabels();

    boolean addLabelToIssue(Integer issueId, Integer labelId);

    boolean removeLabelFromIssue(Integer issueId, Integer labelId);

    List<Label> getLabelsForIssue(Integer issueId);
}
