package ua.duikt.learning.java.pro.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;

/**
 * Created by Mykyta Sirobaba on 13.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {
    private Integer id;
    private Integer projectId;
    private Integer userId;
    private ProjectRoleType role;
}