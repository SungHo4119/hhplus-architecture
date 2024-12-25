package io.hhplus_architecture.unit.untils;

import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.DATE_IS_INVALID;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.ID_IS_INVALID;
import static io.hhplus_architecture.utils.Validation.inputValidationId;
import static io.hhplus_architecture.utils.Validation.isValidDateFormat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.hhplus_architecture.domain.exception.constom.BadRequestException;
import org.junit.jupiter.api.Test;

public class ValidationUnitTest {

    @Test
    public void inputValidationIdCheck_성공() {
        // ginven
        Long id = 1L;
        String fieldName = "id";
        // when
        assertDoesNotThrow(() -> inputValidationId(id, fieldName));
        // then
    }

    @Test
    public void inputValidationIdCheck_BadRequestExceptionIsNull() {
        // ginven
        Long id = null;
        String fieldName = "id";
        // when
        Exception e = assertThrows(BadRequestException.class,
            () -> inputValidationId(id, fieldName));
        //then
        assertEquals(fieldName + ID_IS_INVALID, e.getMessage());
    }

    @Test
    public void inputValidationIdCheck_BadRequestExceptionIsZero() {
        // ginven
        Long id = 0L;
        String fieldName = "id";
        // when
        Exception e = assertThrows(BadRequestException.class,
            () -> inputValidationId(id, fieldName));
        //then
        assertEquals(fieldName + ID_IS_INVALID, e.getMessage());
    }

    @Test
    public void isValidDateFormat_성공() {
        // ginven
        String date = "2021-08-01";
        // when
        assertDoesNotThrow(() -> isValidDateFormat(date));
        // then
    }

    @Test
    public void isValidDateFormat_실패() {
        // ginven
        String date = "2021-08-01T00:00:00";
        // when
        Exception e = assertThrows(BadRequestException.class,
            () -> isValidDateFormat(date));
        //then
        assertEquals(DATE_IS_INVALID, e.getMessage());
    }
}
