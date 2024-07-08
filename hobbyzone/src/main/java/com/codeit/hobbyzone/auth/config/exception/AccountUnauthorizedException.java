package com.codeit.hobbyzone.auth.config.exception;

import com.codeit.hobbyzone.common.exception.base.auth.AuthClientException;
import com.codeit.hobbyzone.common.exception.code.AuthErrorCode;

public class AccountUnauthorizedException extends AuthClientException {

    public AccountUnauthorizedException() {
        super(AuthErrorCode.UNAUTHORIZED);
    }
}
