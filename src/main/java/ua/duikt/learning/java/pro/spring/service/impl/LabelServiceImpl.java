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
