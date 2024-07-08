package com.codeit.hobbyzone.auth.domain.exception;

import com.codeit.hobbyzone.common.exception.base.auth.AuthClientException;
import com.codeit.hobbyzone.common.exception.code.AuthErrorCode;

public class EmptyTokenException extends AuthClientException {

    public EmptyTokenException() {
        super(AuthErrorCode.EMPTY_TOKEN);
    }
}
