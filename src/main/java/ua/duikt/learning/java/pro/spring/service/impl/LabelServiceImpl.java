package ua.duikt.learning.java.pro.spring.service.impl;

import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.service.LabelService;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
// TODO: Implements all necessary methods
public class LabelServiceImpl implements LabelService {
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
