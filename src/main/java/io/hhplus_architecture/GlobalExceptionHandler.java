package io.hhplus_architecture;

import io.hhplus_architecture.domain.exception.ErrorResponse;
import io.hhplus_architecture.domain.exception.constom.AlreadyExistsException;
import io.hhplus_architecture.domain.exception.constom.BadRequestException;
import io.hhplus_architecture.domain.exception.constom.ConflictException;
import io.hhplus_architecture.domain.exception.constom.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("400", e.getMessage()));
    }
    // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("404", e.getMessage()));
    }

    // 409
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("409", e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("409", e.getMessage()));
    }

    // 500
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("500", "에러가 발생했습니다."));
    }
}
