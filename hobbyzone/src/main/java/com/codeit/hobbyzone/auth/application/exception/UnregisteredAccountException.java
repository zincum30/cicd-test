package com.codeit.hobbyzone.auth.application.exception;

import com.codeit.hobbyzone.common.exception.base.auth.AuthClientException;
import com.codeit.hobbyzone.common.exception.code.AuthErrorCode;

public class UnregisteredAccountException extends AuthClientException {

    public UnregisteredAccountException() {
        super(AuthErrorCode.UNREGISTERED_ACCOUNT);
    }
}
