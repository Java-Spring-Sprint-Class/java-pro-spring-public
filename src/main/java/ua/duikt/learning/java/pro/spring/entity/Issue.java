package ua.duikt.learning.java.pro.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;

import java.time.LocalDateTime;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
    private Integer id;
    private Integer projectId;
    private String key;
    private String title;
    private String description;
    private IssueType type;
    private Priority priority;
    private Integer statusId;
    private Integer assigneeId;
    private Integer reporterId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
