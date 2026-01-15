package ua.duikt.learning.java.pro.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ua.duikt.learning.java.pro.spring.entity.enums.StatusCategory;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Data
@Builder
@AllArgsConstructor
public class CreateStatusRequest {
    private Long projectId;
    private String name;
    private StatusCategory category;

}
