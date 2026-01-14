package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.Attachment;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;
import ua.duikt.learning.java.pro.spring.entity.IssueLabel;
import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.service.DetailsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class DetailsServiceImpl implements DetailsService {
    private final Map<Long, IssueComment> commentTable = new ConcurrentHashMap<>();
    private final Map<Long, Attachment> attachmentTable = new ConcurrentHashMap<>();
    private final Map<Long, Label> labelTable = new ConcurrentHashMap<>();
    private final List<IssueLabel> issueLabelTable = new ArrayList<>();

    private final AtomicLong commentIdGen = new AtomicLong(1);
    private final AtomicLong attachmentIdGen = new AtomicLong(1);
    private final AtomicLong labelIdGen = new AtomicLong(1);

    public boolean addComment(Long issueId, String content) {
        Long id = commentIdGen.getAndIncrement();
        IssueComment comment = IssueComment.builder()
                .id(id)
                .issueId(issueId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        commentTable.put(id, comment);
        return true;
    }

    public List<IssueComment> getComments(Long issueId) {
        return commentTable.values().stream()
                .filter(c -> c.getIssueId().equals(issueId))
                .toList();
    }

    public void updateComment(Long id, String content) {
        IssueComment c = commentTable.get(id);
        if (c != null) {
            c.setContent(content);
            c.setUpdatedAt(LocalDateTime.now());
        }
    }

    public boolean deleteComment(Long id) {
        return commentTable.remove(id) != null;
    }

    public boolean addAttachment(Long issueId, String fileName, String fileUrl, Integer fileSize) {
        Long id = attachmentIdGen.getAndIncrement();
        Attachment attachment = Attachment.builder()
                .id(id)
                .issueId(issueId)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileSize(fileSize)
                .createdAt(LocalDateTime.now())
                .build();
        attachmentTable.put(id, attachment);
        return true;
    }

    public List<Attachment> getAttachments(Long issueId) {
        return attachmentTable.values().stream()
                .filter(a -> a.getIssueId().equals(issueId))
                .toList();
    }

    public boolean deleteAttachment(Long id) {
        return attachmentTable.remove(id) != null;
    }

    public Long createLabel(String name, String color) {
        Long id = labelIdGen.getAndIncrement();
        Label label = Label.builder().id(id).name(name).color(color).build();
        labelTable.put(id, label);
        return id;
    }

    public List<Label> getLabels() {
        return new ArrayList<>(labelTable.values());
    }

    public boolean addLabelToIssue(Long issueId, Long labelId) {
        IssueLabel il = IssueLabel.builder().issueId(issueId).labelId(labelId).build();
        return issueLabelTable.add(il);
    }

    public boolean removeLabelFromIssue(Long issueId, Long labelId) {
        return issueLabelTable.removeIf(il ->
                il.getIssueId().equals(issueId) && il.getLabelId().equals(labelId));
    }

    public List<Label> getLabelsForIssue(Long issueId) {
        List<Long> labelIds = issueLabelTable.stream()
                .filter(il -> il.getIssueId().equals(issueId))
                .map(IssueLabel::getLabelId)
                .toList();

        return labelTable.values().stream()
                .filter(l -> labelIds.contains(l.getId()))
                .toList();
    }
}
