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
public class Project {
    private Integer id;
    private String name;
    private String key;
    private Integer ownerId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}