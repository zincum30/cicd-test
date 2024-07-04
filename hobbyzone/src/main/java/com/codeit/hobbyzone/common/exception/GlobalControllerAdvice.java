package com.codeit.hobbyzone.common.exception;

import com.codeit.hobbyzone.common.exception.dto.BaseExceptionDto;
import com.codeit.hobbyzone.common.exception.dto.ParameterExceptionDto;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "%s : ";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> test() {
        return ResponseEntity.internalServerError().build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
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

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<BaseExceptionDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        return processWarningExceptionHandle(ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    private ResponseEntity<BaseExceptionDto> handleIllegalStateException(IllegalStateException ex) {
        return processErrorExceptionHandle(ex);
    }

    private ResponseEntity<BaseExceptionDto> processWarningExceptionHandle(Exception ex) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionStatus exceptionStatus = ExceptionStatus.find(ex.getClass());

        return ResponseEntity.status(exceptionStatus.getHttpStatus())
                             .body(new BaseExceptionDto(exceptionStatus.getCode(), exceptionStatus.getMessage()));
    }

    private ResponseEntity<BaseExceptionDto> processErrorExceptionHandle(Exception ex) {
        logger.error(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionStatus exceptionStatus = ExceptionStatus.find(ex.getClass());

        return ResponseEntity.status(exceptionStatus.getHttpStatus())
                             .body(new BaseExceptionDto(exceptionStatus.getCode(), exceptionStatus.getMessage()));
    }
}
