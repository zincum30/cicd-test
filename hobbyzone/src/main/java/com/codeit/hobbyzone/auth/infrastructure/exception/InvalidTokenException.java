package com.codeit.hobbyzone.auth.infrastructure.exception;

import com.codeit.hobbyzone.common.exception.base.auth.AuthClientException;
import com.codeit.hobbyzone.common.exception.code.AuthErrorCode;

public class InvalidTokenException extends AuthClientException {

    public InvalidTokenException() {
        super(AuthErrorCode.INVALID_TOKEN);
    }
}
