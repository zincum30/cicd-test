package com.codeit.hobbyzone.auth.application.exception;

import com.codeit.hobbyzone.common.exception.base.auth.AuthClientException;
import com.codeit.hobbyzone.common.exception.code.AuthErrorCode;

public class PasswordMismatchException extends AuthClientException {

    public PasswordMismatchException() {
        super(AuthErrorCode.PASSWORD_MISMATCH);
    }
}
