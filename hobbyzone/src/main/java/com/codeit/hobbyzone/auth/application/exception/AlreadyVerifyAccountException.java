package com.codeit.hobbyzone.auth.application.exception;

import com.codeit.hobbyzone.common.exception.base.auth.SignUpClientException;
import com.codeit.hobbyzone.common.exception.code.SignUpErrorCode;

public class AlreadyVerifyAccountException extends SignUpClientException {

    public AlreadyVerifyAccountException() {
        super(SignUpErrorCode.ALREADY_SIGNED_UP);
    }
}
