package ua.duikt.learning.java.pro.spring.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.duikt.learning.java.pro.spring.entity.enums.ProjectRoleType;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Role type is required")
    private ProjectRoleType role;
}
