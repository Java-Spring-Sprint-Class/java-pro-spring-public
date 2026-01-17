package ua.duikt.learning.java.pro.spring.exceptions;

/**
 * Created by Mykyta Sirobaba on 17.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
