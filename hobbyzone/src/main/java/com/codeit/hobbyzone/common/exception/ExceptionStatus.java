package com.codeit.hobbyzone.common.exception;

import com.codeit.hobbyzone.account.domain.exception.InvalidNicknameException;
import com.codeit.hobbyzone.account.domain.exception.InvalidPasswordEncoderException;
import com.codeit.hobbyzone.account.domain.exception.InvalidPasswordException;
import com.codeit.hobbyzone.auth.application.exception.AlreadyVerifyAccountException;
import com.codeit.hobbyzone.auth.application.exception.DuplicateNicknameException;
import com.codeit.hobbyzone.auth.application.exception.NotVerifyAccountException;
import com.codeit.hobbyzone.auth.application.exception.VerifyAccountNotFoundException;
import com.codeit.hobbyzone.auth.domain.exception.ExpiredVerifyCodeException;
import com.codeit.hobbyzone.auth.domain.exception.VerifyFailedException;
import com.codeit.hobbyzone.auth.infrastructure.exception.MailSendFailedException;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public enum ExceptionStatus {
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(
            MethodArgumentNotValidException.class,
            HttpStatus.BAD_REQUEST,
            "VALIDATION_ERROR",
            "유효한 파라미터 값을 입력해주세요."
    ),
    VERIFY_ACCOUNT_NOT_FOUND_EXCEPTION(
            VerifyAccountNotFoundException.class,
            HttpStatus.BAD_REQUEST,
            "NOT_VERIFICATION",
            "아직 메일 인증을 진행하지 않았습니다."
    ),
    NOT_VERIFY_ACCOUNT_EXCEPTION(
            NotVerifyAccountException.class,
            HttpStatus.BAD_REQUEST,
            "NOT_VERIFICATION",
            "아직 메일 인증을 진행하지 않았습니다."
    ),
    INVALID_PASSWORD_EXCEPTION(
            InvalidPasswordException.class,
            HttpStatus.BAD_REQUEST,
            "VALIDATION_ERROR",
            "비밀번호는 영어와 숫자를 포함해 8글자 이상 입력해주세요."
    ),
    INVALID_NICKNAME_EXCEPTION(
            InvalidNicknameException.class,
            HttpStatus.BAD_REQUEST,
            "VALIDATION_ERROR",
            "닉네임은 2글자 이상 8글자 이하로 입력해주세요."
    ),
    INVALID_PASSWORD_ENCODER_EXCEPTION(
            InvalidPasswordEncoderException.class,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "SERVER_ERROR",
            "서버에 문제가 발생했습니다."
    ),
    ALREADY_VERIFY_ACCOUNT_EXCEPTION(
            AlreadyVerifyAccountException.class,
            HttpStatus.BAD_REQUEST,
            "ALREADY_REQUEST",
            "이미 인증 메일을 요청했거나 이미 회원 가입한 이메일입니다."
    ),
    MAIL_SEND_FAILED_EXCEPTION(
            MailSendFailedException.class,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "SERVER_ERROR",
            "서버에 문제가 발생했습니다."
    ),
    VERIFY_FAILED_EXCEPTION(
            VerifyFailedException.class,
            HttpStatus.BAD_REQUEST,
            "VERIFICATION_FAIL",
            "인증 코드가 일치하지 않습니다."
    ),
    EXPIRED_VERIFY_CODE_EXCEPTION(
            ExpiredVerifyCodeException.class,
            HttpStatus.BAD_REQUEST,
            "VERIFICATION_FAIL",
            "유효한 인증 코드가 아닙니다."
    ),
    DUPLICATE_NICKNAME_EXCEPTION(
            DuplicateNicknameException.class,
            HttpStatus.BAD_REQUEST,
            "DUPLICATE_NICKNAME",
            "이미 존재하는 닉네임입니다."
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
                     .orElseThrow(() -> new IllegalStateException("정의되지 않은 예외 타입입니다."));
    }
}
