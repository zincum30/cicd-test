package com.codeit.hobbyzone.common.exception.translate;

import com.codeit.hobbyzone.common.exception.code.AccountErrorCode;
import com.codeit.hobbyzone.common.exception.dto.BaseExceptionDto;
import java.util.Arrays;
import org.springframework.http.HttpStatus;

public enum AccountExceptionTranslator {
    INVALID_PASSWORD_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            AccountErrorCode.VALIDATION_PASSWORD_ERROR,
            "비밀번호는 영어와 숫자를 포함해 8글자 이상 입력해주세요."
    ),
    INVALID_NICKNAME_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            AccountErrorCode.VALIDATION_NICKNAME_ERROR,
            "닉네임은 2글자 이상 8글자 이하로 입력해주세요."
    ),
    INVALID_PASSWORD_ENCODER_EXCEPTION(
            HttpStatus.INTERNAL_SERVER_ERROR,
            AccountErrorCode.SERVER_ERROR,
            "서버에 문제가 발생했습니다."
    ),
    ;

    private final HttpStatus httpStatus;
    private final AccountErrorCode errorCode;
    private final String message;

    AccountExceptionTranslator(HttpStatus httpStatus, AccountErrorCode errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static AccountExceptionTranslator find(AccountErrorCode errorCode) {
        return Arrays.stream(AccountExceptionTranslator.values())
                     .filter(translator -> translator.errorCode.equals(errorCode))
                     .findAny()
                     .orElseThrow(() -> new IllegalStateException("정의되지 않은 예외입니다."));
    }

    public HttpStatus status() {
        return this.httpStatus;
    }

    public BaseExceptionDto translate() {
        return new BaseExceptionDto(this.errorCode.toString(), this.message);
    }
}
