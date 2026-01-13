package ua.duikt.learning.java.pro.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ua.duikt.learning.java.pro.spring.entity.enums.IssueType;
import ua.duikt.learning.java.pro.spring.entity.enums.Priority;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Data
@Builder
@AllArgsConstructor
public class CreateIssueRequest {
    private Integer projectId;
    private String title;
    private String description;
    private IssueType type;
    private Priority priority;
}
