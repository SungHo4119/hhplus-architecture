package io.hhplus_architecture.utils;

import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.DATE_IS_INVALID;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.ID_IS_INVALID;

import io.hhplus_architecture.domain.exception.constom.BadRequestException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class Validation {

    private Validation() {
    }

    public static void inputValidationId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new BadRequestException(fieldName + ID_IS_INVALID);
        }
    }

    public static void isValidDateFormat(String date) {
        // 'YYYY-MM-DD' 포맷의 DateTimeFormatter 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // 주어진 문자열을 LocalDate로 파싱
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new BadRequestException(DATE_IS_INVALID);
        }
    }

}
