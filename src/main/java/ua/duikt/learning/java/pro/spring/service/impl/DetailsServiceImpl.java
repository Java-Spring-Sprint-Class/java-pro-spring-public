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
    public boolean addComment(Integer issueId, String content)  {
        return false;
    }
    // TODO: Implements the method
    @Override
    public List<IssueComment> getComments(Integer issueId) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public void updateComment(Integer id, String content) {

    }
    // TODO: Implements the method
    @Override
    public boolean deleteComment(Integer id) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public boolean addAttachment(Integer issueId, String fileName, String fileUrl, Integer fileSize) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public List<Attachment> getAttachments(Integer issueId) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public boolean deleteAttachment(Integer id) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public Integer createLabel(String name, String color) {
        return null;
    }
    // TODO: Implements the method
    @Override
    public List<Label> getLabels() {
        return null;
    }
    // TODO: Implements the method
    @Override
    public boolean addLabelToIssue(Integer issueId, Integer labelId) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public boolean removeLabelFromIssue(Integer issueId, Integer labelId) {
        return false;
    }
    // TODO: Implements the method
    @Override
    public List<Label> getLabelsForIssue(Integer issueId) {
        return null;
    }
}
