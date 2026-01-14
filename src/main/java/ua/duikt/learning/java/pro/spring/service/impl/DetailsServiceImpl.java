package ua.duikt.learning.java.pro.spring.service.impl;

import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.service.DetailsService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
// TODO: Implements all necessary methods
public class DetailsServiceImpl implements DetailsService {
    // TODO: Implements the method
    @Override
    public boolean addComment(Long issueId, String content) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public List<IssueComment> getComments(Long issueId) {
        return List.of();
    }

    // TODO: Implements the method
    @Override
    public void updateComment(Long id, String content) {

    }

    // TODO: Implements the method
    @Override
    public boolean deleteComment(Long id) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public boolean addAttachment(Long issueId, String fileName, String fileUrl, Integer fileSize) {
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

    // TODO: Implements the method
    @Override
    public Long createLabel(String name, String color) {
        return 0L;
    }

    // TODO: Implements the method
    @Override
    public List<Label> getLabels() {
        return List.of();
    }

    // TODO: Implements the method
    @Override
    public boolean addLabelToIssue(Long issueId, Long labelId) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public boolean removeLabelFromIssue(Long issueId, Long labelId) {
        return false;
    }

    // TODO: Implements the method
    @Override
    public List<Label> getLabelsForIssue(Long issueId) {
        return List.of();
    }
}
