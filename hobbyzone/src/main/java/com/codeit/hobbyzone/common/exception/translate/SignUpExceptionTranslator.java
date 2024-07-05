package com.codeit.hobbyzone.common.exception.translate;

import com.codeit.hobbyzone.common.exception.code.SignUpErrorCode;
import com.codeit.hobbyzone.common.exception.dto.BaseExceptionDto;
import java.util.Arrays;
import org.springframework.http.HttpStatus;

public enum SignUpExceptionTranslator {
    VERIFY_ACCOUNT_NOT_FOUND_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            SignUpErrorCode.NOT_VERIFICATION,
            "아직 메일 인증을 진행하지 않았습니다."
    ),
    NOT_VERIFY_ACCOUNT_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            SignUpErrorCode.NOT_VERIFICATION,
            "아직 메일 인증을 진행하지 않았습니다."
    ),
    ALREADY_VERIFY_ACCOUNT_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            SignUpErrorCode.ALREADY_SIGNED_UP,
            "이미 회원 가입한 이메일입니다."
    ),
    MAIL_SEND_FAILED_EXCEPTION(
            HttpStatus.INTERNAL_SERVER_ERROR,
            SignUpErrorCode.SERVER_ERROR,
            "서버에 문제가 발생했습니다."
    ),
    VERIFY_FAILED_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            SignUpErrorCode.VERIFICATION_FAILED,
            "인증 코드가 일치하지 않습니다."
    ),
    EXPIRED_VERIFY_CODE_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            SignUpErrorCode.EXPIRED_VERIFY_CODE,
            "유효한 인증 코드가 아닙니다."
    ),
    DUPLICATE_NICKNAME_EXCEPTION(
            HttpStatus.BAD_REQUEST,
            SignUpErrorCode.DUPLICATE_NICKNAME,
            "이미 존재하는 닉네임입니다."
    )
    ;

    private final HttpStatus httpStatus;
    private final SignUpErrorCode errorCode;
    private final String message;

    SignUpExceptionTranslator(HttpStatus httpStatus, SignUpErrorCode errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static SignUpExceptionTranslator find(SignUpErrorCode errorCode) {
        return Arrays.stream(SignUpExceptionTranslator.values())
                     .filter(translator -> translator.errorCode == errorCode)
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
