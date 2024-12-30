package io.hhplus_architecture.domain.exception;

public record ErrorResponse(
    String code,
    String message
) {
}