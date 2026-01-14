package ua.duikt.learning.java.pro.spring.service;

import ua.duikt.learning.java.pro.spring.entity.Label;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface LabelService {
    Integer createLabel(String name, String color);

    List<Label> getLabels();

    boolean addLabelToIssue(Integer issueId, Integer labelId);

    boolean removeLabelFromIssue(Integer issueId, Integer labelId);

    List<Label> getLabelsForIssue(Integer issueId);
}
