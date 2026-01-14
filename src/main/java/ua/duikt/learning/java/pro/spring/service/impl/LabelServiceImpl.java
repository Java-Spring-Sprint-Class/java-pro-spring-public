package ua.duikt.learning.java.pro.spring.service.impl;

import org.springframework.stereotype.Service;
import ua.duikt.learning.java.pro.spring.entity.IssueLabel;
import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.service.LabelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Service
public class LabelServiceImpl implements LabelService {
    private final Map<Integer, Label> labelTable = new ConcurrentHashMap<>();
    private final List<IssueLabel> issueLabelTable = new ArrayList<>();
    private final AtomicInteger labelIdGen = new AtomicInteger(1);

    public Integer createLabel(String name, String color) {
        Integer id = labelIdGen.getAndIncrement();
        Label label = Label.builder().id(id).name(name).color(color).build();
        labelTable.put(id, label);
        return id;
    }

    public List<Label> getLabels() {
        return new ArrayList<>(labelTable.values());
    }

    public boolean addLabelToIssue(Integer issueId, Integer labelId) {
        IssueLabel il = IssueLabel.builder().issueId(issueId).labelId(labelId).build();
        return issueLabelTable.add(il);
    }

    public boolean removeLabelFromIssue(Integer issueId, Integer labelId) {
        return issueLabelTable.removeIf(il ->
                il.getIssueId().equals(issueId) && il.getLabelId().equals(labelId));
    }

    public List<Label> getLabelsForIssue(Integer issueId) {
        List<Integer> labelIds = issueLabelTable.stream()
                .filter(il -> il.getIssueId().equals(issueId))
                .map(IssueLabel::getLabelId)
                .toList();

        return labelTable.values().stream()
                .filter(l -> labelIds.contains(l.getId()))
                .toList();
    }
}
