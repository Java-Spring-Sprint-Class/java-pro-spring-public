package ua.duikt.learning.java.pro.spring.sprint03.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.duikt.learning.java.pro.spring.entity.IssueLabel;
import ua.duikt.learning.java.pro.spring.entity.Label;
import ua.duikt.learning.java.pro.spring.repositories.IssueLabelRepo;
import ua.duikt.learning.java.pro.spring.repositories.LabelRepo;
import ua.duikt.learning.java.pro.spring.service.impl.LabelServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class LabelServiceTest {

    @Mock
    private LabelRepo labelRepo;

    @Mock
    private IssueLabelRepo issueLabelRepo;

    @InjectMocks
    private LabelServiceImpl labelService;

    @Test
    @DisplayName("createLabel: should save label and return its id")
    void createLabel_shouldSaveLabelAndReturnId() {
        
        Label savedLabel = Label.builder()
                .id(1L)
                .name("bug")
                .color("#FF0000")
                .build();

        when(labelRepo.save(any(Label.class))).thenReturn(savedLabel);

        ArgumentCaptor<Label> captor = ArgumentCaptor.forClass(Label.class);

        
        Long result = labelService.createLabel("bug", "#FF0000");

        
        assertThat(result).isEqualTo(1L);
        verify(labelRepo).save(captor.capture());

        Label label = captor.getValue();
        assertThat(label.getName()).isEqualTo("bug");
        assertThat(label.getColor()).isEqualTo("#FF0000");
    }

    @Test
    @DisplayName("getLabels: should return all labels")
    void getLabels_shouldReturnAllLabels() {
         
        List<Label> labels = List.of(
                Label.builder().id(1L).build(),
                Label.builder().id(2L).build()
        );

        when(labelRepo.findAll()).thenReturn(labels);

        List<Label> result = labelService.getLabels();

        assertThat(result).hasSize(2);
        verify(labelRepo).findAll();
    }

    @Test
    @DisplayName("addLabelToIssue: should add label to issue when relation does not exist")
    void addLabelToIssue_shouldSaveIssueLabel_ifNotExists() {
         
        Long issueId = 10L;
        Long labelId = 5L;

        when(issueLabelRepo.existsByIssueIdAndLabelId(issueId, labelId))
                .thenReturn(false);

        ArgumentCaptor<IssueLabel> captor = ArgumentCaptor.forClass(IssueLabel.class);

        boolean result = labelService.addLabelToIssue(issueId, labelId);

        assertThat(result).isTrue();
        verify(issueLabelRepo).save(captor.capture());

        IssueLabel saved = captor.getValue();
        assertThat(saved.getIssueId()).isEqualTo(issueId);
        assertThat(saved.getLabelId()).isEqualTo(labelId);
    }

    @Test
    @DisplayName("addLabelToIssue: should return false when relation already exists")
    void addLabelToIssue_shouldReturnFalse_ifExists() {
         
        Long issueId = 10L;
        Long labelId = 5L;

        when(issueLabelRepo.existsByIssueIdAndLabelId(issueId, labelId))
                .thenReturn(true);

        boolean result = labelService.addLabelToIssue(issueId, labelId);

        assertThat(result).isFalse();
        verify(issueLabelRepo, never()).save(any());
    }

    @Test
    @DisplayName("removeLabelFromIssue: should delete relation and return true if exists")
    void removeLabelFromIssue_shouldDeleteAndReturnTrue_ifExists() {
         
        Long issueId = 7L;
        Long labelId = 3L;

        when(issueLabelRepo.existsByIssueIdAndLabelId(issueId, labelId))
                .thenReturn(true);

        boolean result = labelService.removeLabelFromIssue(issueId, labelId);

        assertThat(result).isTrue();
        verify(issueLabelRepo).deleteByIssueIdAndLabelId(issueId, labelId);
    }

    @Test
    @DisplayName("removeLabelFromIssue: should return false if relation does not exist")
    void removeLabelFromIssue_shouldReturnFalse_ifNotExists() {
         
        Long issueId = 7L;
        Long labelId = 3L;

        when(issueLabelRepo.existsByIssueIdAndLabelId(issueId, labelId))
                .thenReturn(false);

        boolean result = labelService.removeLabelFromIssue(issueId, labelId);

        assertThat(result).isFalse();
        verify(issueLabelRepo, never())
                .deleteByIssueIdAndLabelId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("getLabelsForIssue: should return labels for issue")
    void getLabelsForIssue_shouldReturnLabels() {
         
        Long issueId = 4L;
        List<Label> labels = List.of(
                Label.builder().id(1L).name("bug").build(),
                Label.builder().id(2L).name("feature").build()
        );

        when(labelRepo.findAllByIssueId(issueId)).thenReturn(labels);

        List<Label> result = labelService.getLabelsForIssue(issueId);

        assertThat(result).hasSize(2);
        verify(labelRepo).findAllByIssueId(issueId);
    }
}
