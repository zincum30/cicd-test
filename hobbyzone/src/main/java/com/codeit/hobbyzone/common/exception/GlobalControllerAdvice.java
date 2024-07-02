package com.codeit.hobbyzone.common.exception;

import com.codeit.hobbyzone.common.exception.dto.BaseExceptionDto;
import com.codeit.hobbyzone.common.exception.dto.ParameterExceptionDto;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

        String parameters = ex.getFieldErrors()
                           .stream()
                           .map(FieldError::getField)
                           .collect(Collectors.joining(", "));
        ExceptionStatus exceptionStatus = ExceptionStatus.find(ex.getClass());

        return ResponseEntity.status(exceptionStatus.getHttpStatus())
                             .body(
                                     new ParameterExceptionDto(
                                             exceptionStatus.getCode(),
                                             parameters,
                                             exceptionStatus.getMessage()
                                     )
                             );
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionStatus exceptionStatus = ExceptionStatus.find(ex.getClass());

        return ResponseEntity.status(exceptionStatus.getHttpStatus())
                             .body(new BaseExceptionDto(exceptionStatus.getCode(), exceptionStatus.getMessage()));
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
}
