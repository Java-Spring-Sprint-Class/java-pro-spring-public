package ua.duikt.learning.java.pro.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Data
@Builder
@AllArgsConstructor
public class AddAttachmentRequest {
    private String fileName;
    private String fileUrl;
    private Integer fileSize;
}
