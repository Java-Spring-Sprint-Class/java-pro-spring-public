package ua.duikt.learning.java.pro.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueHistory {
    private Long id;
    private Long issueId;
    private Long userId;
    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdAt;
}
