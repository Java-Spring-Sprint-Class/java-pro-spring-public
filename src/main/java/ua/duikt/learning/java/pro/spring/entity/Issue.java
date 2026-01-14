package ua.duikt.learning.java.pro.spring.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "issue")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column
    private IssueType type;

    @Enumerated(EnumType.STRING)
    @Column
    private Priority priority;

    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "reporter_id")
    private Long reporterId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
