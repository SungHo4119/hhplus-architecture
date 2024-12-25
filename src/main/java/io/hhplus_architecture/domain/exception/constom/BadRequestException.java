package io.hhplus_architecture.domain.exception.constom;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
