package com.codeit.hobbyzone.common.exception.translate;

import com.codeit.hobbyzone.common.exception.code.AuthErrorCode;
import com.codeit.hobbyzone.common.exception.dto.BaseExceptionDto;
import java.util.Arrays;
import org.springframework.http.HttpStatus;

public enum AuthExceptionTranslator {
    EMPTY_TOKEN_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            AuthErrorCode.EMPTY_TOKEN,
            "토큰을 입력해주세요."
    ),
    REFRESH_TOKEN_NOT_FOUND(
            HttpStatus.BAD_REQUEST,
            AuthErrorCode.REFRESH_TOKEN_NOT_FOUND,
            "리프레시 토큰을 입력해주세요."
    ),
    INVALID_TOKEN_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            AuthErrorCode.INVALID_TOKEN,
            "유효한 토큰이 아닙니다."
    ),
    PASSWORD_MISMATCH_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            AuthErrorCode.PASSWORD_MISMATCH,
            "로그인에 실패했습니다."
    ),
    UNREGISTERED_ACCOUNT_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            AuthErrorCode.UNREGISTERED_ACCOUNT,
            "로그인에 실패했습니다."
    ),
    ACCOUNT_UNAUTHORIZED_EXCEPTION(
            HttpStatus.UNAUTHORIZED,
            AuthErrorCode.UNAUTHORIZED,
            "로그인이 필요한 기능입니다."
    ),
    ;

    private final HttpStatus httpStatus;
    private final AuthErrorCode errorCode;
    private final String message;

    AuthExceptionTranslator(HttpStatus httpStatus, AuthErrorCode errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static AuthExceptionTranslator find(AuthErrorCode errorCode) {
        return Arrays.stream(AuthExceptionTranslator.values())
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
