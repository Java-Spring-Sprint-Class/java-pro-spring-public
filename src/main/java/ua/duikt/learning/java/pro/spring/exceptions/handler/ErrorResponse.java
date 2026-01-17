package ua.duikt.learning.java.pro.spring.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * Created by Mykyta Sirobaba on 17.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
