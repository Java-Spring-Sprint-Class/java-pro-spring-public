package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.duikt.learning.java.pro.spring.entity.IssueLabel;
import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.exceptions.ConflictException;
import ua.duikt.learning.java.pro.spring.exceptions.ResourceNotFoundException;
import ua.duikt.learning.java.pro.spring.repositories.IssueLabelRepo;
import ua.duikt.learning.java.pro.spring.repositories.LabelRepo;
import ua.duikt.learning.java.pro.spring.service.LabelService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepo labelRepo;
    private final IssueLabelRepo issueLabelRepo;

    @Override
    @Transactional
    public Long createLabel(String name, String color) {
        Label label = Label.builder()
                .name(name)
                .color(color)
                .build();
        return labelRepo.save(label).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Label> getLabels() {
        return labelRepo.findAll();
    }

    @Override
    @Transactional
    public void addLabelToIssue(Long issueId, Long labelId) {
        if (issueLabelRepo.existsByIssueIdAndLabelId(issueId, labelId)) {
            throw new ConflictException("Label already exists");
        }

        IssueLabel issueLabel = IssueLabel.builder()
                .issueId(issueId)
                .labelId(labelId)
                .build();

        issueLabelRepo.save(issueLabel);
    }

    @Override
    @Transactional
    public void removeLabelFromIssue(Long issueId, Long labelId) {
        if (!issueLabelRepo.existsByIssueIdAndLabelId(issueId, labelId)) {
            throw new ResourceNotFoundException("Label does not exists");
        }
        issueLabelRepo.deleteByIssueIdAndLabelId(issueId, labelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Label> getLabelsForIssue(Long issueId) {
        return labelRepo.findAllByIssueId(issueId);
    }
}