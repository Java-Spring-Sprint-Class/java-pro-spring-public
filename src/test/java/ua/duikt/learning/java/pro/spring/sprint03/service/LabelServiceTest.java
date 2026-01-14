package ua.duikt.learning.java.pro.spring.sprint03.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.service.LabelService;
import ua.duikt.learning.java.pro.spring.service.impl.LabelServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
class LabelServiceTest {

    private LabelService labelService;

    @BeforeEach
    void setUp() {
        labelService = new LabelServiceImpl();
    }

    @Test
    @DisplayName("Labels Logic: Create, Get All, Link, Unlink")
    void labelsLogic() {
        Integer l1 = labelService.createLabel("Backend", "Blue");
        Integer l2 = labelService.createLabel("Frontend", "Red");

        List<Label> allLabels = labelService.getLabels();
        assertThat(allLabels).hasSize(2);

        labelService.addLabelToIssue(100, l1);
        labelService.addLabelToIssue(100, l2);

        List<Label> issueLabels = labelService.getLabelsForIssue(100);
        assertThat(issueLabels).hasSize(2);
        assertThat(issueLabels).extracting(Label::getName).contains("Backend", "Frontend");

        assertThat(labelService.getLabelsForIssue(999)).isEmpty();

        boolean isRemoved = labelService.removeLabelFromIssue(100, l1);
        assertThat(isRemoved).isTrue();

        List<Label> labelsAfterRemoval = labelService.getLabelsForIssue(100);
        assertThat(labelsAfterRemoval).hasSize(1);
        assertThat(labelsAfterRemoval.getFirst().getId()).isEqualTo(l2);
    }
}
