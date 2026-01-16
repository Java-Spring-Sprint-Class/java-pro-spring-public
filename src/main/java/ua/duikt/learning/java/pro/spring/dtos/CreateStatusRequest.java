package ua.duikt.learning.java.pro.spring.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateStatusRequest {
    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotBlank(message = "Status name is required")
    private String name;

    @NotNull(message = "Status category is required")
    private StatusCategory category;
}
