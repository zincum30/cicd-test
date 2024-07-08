package com.codeit.hobbyzone.common.exception;

import com.codeit.hobbyzone.common.exception.base.account.AccountClientException;
import com.codeit.hobbyzone.common.exception.base.account.AccountServerException;
import com.codeit.hobbyzone.common.exception.base.auth.AuthClientException;
import com.codeit.hobbyzone.common.exception.base.auth.AuthServerException;
import com.codeit.hobbyzone.common.exception.base.auth.SignUpClientException;
import com.codeit.hobbyzone.common.exception.base.auth.SignUpServerException;
import com.codeit.hobbyzone.common.exception.code.CommonErrorCode;
import com.codeit.hobbyzone.common.exception.dto.BaseExceptionDto;
import com.codeit.hobbyzone.common.exception.translate.AccountExceptionTranslator;
import com.codeit.hobbyzone.common.exception.translate.AuthExceptionTranslator;
import com.codeit.hobbyzone.common.exception.translate.CommonExceptionTranslator;
import com.codeit.hobbyzone.common.exception.translate.SignUpExceptionTranslator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "%s : ";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        CommonExceptionTranslator translator = CommonExceptionTranslator.find(CommonErrorCode.VALIDATION_ERROR);

        return ResponseEntity.status(translator.status())
                             .body(translator.translate(ex.getFieldErrors()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        logger.error(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @ExceptionHandler(SignUpServerException.class)
    private ResponseEntity<BaseExceptionDto> handleSignUpServerException(SignUpServerException ex) {
        logger.error(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        SignUpExceptionTranslator translator = SignUpExceptionTranslator.find(ex.getErrorCode());

        return ResponseEntity.status(translator.status())
                             .body(translator.translate());
    }

    @ExceptionHandler(SignUpClientException.class)
    private ResponseEntity<BaseExceptionDto> handleSignUpClientException(SignUpClientException ex) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        SignUpExceptionTranslator translator = SignUpExceptionTranslator.find(ex.getErrorCode());

        return ResponseEntity.status(translator.status())
                             .body(translator.translate());
    }

    @ExceptionHandler(AccountServerException.class)
    private ResponseEntity<BaseExceptionDto> handleAccountServerException(AccountServerException ex) {
        logger.error(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        AccountExceptionTranslator translator = AccountExceptionTranslator.find(ex.getErrorCode());

        return ResponseEntity.status(translator.status())
                             .body(translator.translate());
    }

    @ExceptionHandler(AccountClientException.class)
    private ResponseEntity<BaseExceptionDto> handleAccountClientException(AccountClientException ex) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        AccountExceptionTranslator translator = AccountExceptionTranslator.find(ex.getErrorCode());

        return ResponseEntity.status(translator.status())
                             .body(translator.translate());
    }

    @ExceptionHandler(AuthServerException.class)
    private ResponseEntity<BaseExceptionDto> handleAuthServerException(AuthServerException ex) {
        logger.error(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        AuthExceptionTranslator translator = AuthExceptionTranslator.find(ex.getErrorCode());

        return ResponseEntity.status(translator.status())
                             .body(translator.translate());
    }

    @ExceptionHandler(AuthClientException.class)
    private ResponseEntity<BaseExceptionDto> handleAuthClientException(AuthClientException ex) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        AuthExceptionTranslator translator = AuthExceptionTranslator.find(ex.getErrorCode());

        return ResponseEntity.status(translator.status())
                             .body(translator.translate());
    }
}
