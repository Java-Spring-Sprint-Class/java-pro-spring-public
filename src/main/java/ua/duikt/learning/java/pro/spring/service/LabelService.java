package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.Label;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface LabelService {
    Long createLabel(String name, String color);

    List<Label> getLabels();

    boolean addLabelToIssue(Long issueId, Long labelId);

    boolean removeLabelFromIssue(Long issueId, Long labelId);

    List<Label> getLabelsForIssue(Long issueId);
}
