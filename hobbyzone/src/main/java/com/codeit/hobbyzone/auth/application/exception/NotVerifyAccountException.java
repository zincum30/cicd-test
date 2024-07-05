package com.codeit.hobbyzone.auth.application.exception;

import com.codeit.hobbyzone.common.exception.base.auth.SignUpClientException;
import com.codeit.hobbyzone.common.exception.code.SignUpErrorCode;

public class NotVerifyAccountException extends SignUpClientException {

    public NotVerifyAccountException() {
        super(SignUpErrorCode.NOT_VERIFICATION);
    }
}
