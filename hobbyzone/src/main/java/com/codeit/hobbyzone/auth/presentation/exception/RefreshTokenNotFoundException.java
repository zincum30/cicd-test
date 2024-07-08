package com.codeit.hobbyzone.auth.presentation.exception;

import com.codeit.hobbyzone.common.exception.base.auth.AuthClientException;
import com.codeit.hobbyzone.common.exception.code.AuthErrorCode;

public class RefreshTokenNotFoundException extends AuthClientException {

    public RefreshTokenNotFoundException() {
        super(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}
