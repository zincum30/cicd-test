package com.codeit.hobbyzone.common.exception.translate;

import com.codeit.hobbyzone.common.exception.code.CommonErrorCode;
import com.codeit.hobbyzone.common.exception.dto.ParameterExceptionDto;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

public enum CommonExceptionTranslator {
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(
    HttpStatus.BAD_REQUEST,
    CommonErrorCode.VALIDATION_ERROR,
            "유효한 파라미터 값을 입력해주세요."
    ),
    ;

    private static final String PARAMETER_SEPARATOR = ", ";

    private final HttpStatus httpStatus;
    private final CommonErrorCode errorCode;
    private final String message;

    CommonExceptionTranslator(HttpStatus httpStatus, CommonErrorCode errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static CommonExceptionTranslator find(CommonErrorCode errorCode) {
        return Arrays.stream(CommonExceptionTranslator.values())
                     .filter(translator -> translator.errorCode == errorCode)
                     .findAny()
                     .orElseThrow(() -> new IllegalStateException("정의되지 않은 예외입니다."));
    }

    public HttpStatus status() {
        return this.httpStatus;
    }

    public ParameterExceptionDto translate(List<FieldError> errors) {
        String parameters = errors.stream()
                                  .map(FieldError::getField)
                                  .collect(Collectors.joining(PARAMETER_SEPARATOR));

        return new ParameterExceptionDto(this.errorCode.toString(), parameters, this.message);
    }
}
