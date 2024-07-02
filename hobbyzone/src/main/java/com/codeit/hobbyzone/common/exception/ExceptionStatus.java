package com.codeit.hobbyzone.common.exception;

import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public enum ExceptionStatus {
    METHOD_NOT_SUPPORTED_EXCEPTION(
            HttpRequestMethodNotSupportedException.class,
            HttpStatus.METHOD_NOT_ALLOWED,
            "NOT_ALLOWED",
            "허용되지 않은 HTTP Method 입니다."
    ),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(
            MethodArgumentNotValidException.class,
            HttpStatus.BAD_REQUEST,
            "VALIDATION_ERROR",
            "유효한 파라미터 값을 입력해주세요."
    )
    ;

    private final Class<?> exceptionType;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ExceptionStatus(Class<?> exceptionType, HttpStatus httpStatus, String code, String message) {
        this.exceptionType = exceptionType;
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public static ExceptionStatus find(Class<?> exceptionType) {
        return Arrays.stream(ExceptionStatus.values())
                .filter(status -> status.exceptionType.equals(exceptionType))
                .findAny()
                .orElseThrow();
    }
}
