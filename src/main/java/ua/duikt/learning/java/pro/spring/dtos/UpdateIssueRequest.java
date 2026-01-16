package ua.duikt.learning.java.pro.spring.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIssueRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title is too long")
    private String title;

    private String description;
}
