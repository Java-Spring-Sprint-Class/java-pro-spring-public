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
    private Integer id;
    private Integer issueId;
    private Integer userId;
    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdAt;
}
