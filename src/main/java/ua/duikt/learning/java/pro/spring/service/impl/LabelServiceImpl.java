package ua.duikt.learning.java.pro.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepo labelRepo;
    private final IssueLabelRepo issueLabelRepo;

    @Override
    @Transactional
    public Long createLabel(String name, String color) {
        log.info("Creating label with name: {} and color: {}", name, color);
        Label label = Label.builder()
                .name(name)
                .color(color)
                .build();
        Long id = labelRepo.save(label).getId();
        log.info("Label created with ID: {}", id);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Label> getLabels() {
        log.info("Fetching all labels");
        return labelRepo.findAll();
    }

    @Override
    @Transactional
    public void addLabelToIssue(Long issueId, Long labelId) {
        log.info("Adding label ID: {} to issue ID: {}", labelId, issueId);
        if (issueLabelRepo.existsByIssueIdAndLabelId(issueId, labelId)) {
            log.warn("Label ID: {} already assigned to issue ID: {}", labelId, issueId);
            throw new ConflictException("Label already exists");
        }

        IssueLabel issueLabel = IssueLabel.builder()
                .issueId(issueId)
                .labelId(labelId)
                .build();

        issueLabelRepo.save(issueLabel);
        log.info("Label added to issue successfully");
    }

    @Override
    @Transactional
    public void removeLabelFromIssue(Long issueId, Long labelId) {
        log.info("Removing label ID: {} from issue ID: {}", labelId, issueId);
        if (!issueLabelRepo.existsByIssueIdAndLabelId(issueId, labelId)) {
            log.error("Label assignment not found. IssueID: {}, LabelID: {}", issueId, labelId);
            throw new ResourceNotFoundException("Label does not exists");
        }
        issueLabelRepo.deleteByIssueIdAndLabelId(issueId, labelId);
        log.info("Label removed from issue successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Label> getLabelsForIssue(Long issueId) {
        log.info("Fetching labels for issue ID: {}", issueId);
        return labelRepo.findAllByIssueId(issueId);
    }
}