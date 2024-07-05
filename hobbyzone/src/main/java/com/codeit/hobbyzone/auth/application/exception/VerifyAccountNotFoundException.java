package com.codeit.hobbyzone.auth.application.exception;

import com.codeit.hobbyzone.common.exception.base.auth.SignUpClientException;
import com.codeit.hobbyzone.common.exception.code.SignUpErrorCode;

public class VerifyAccountNotFoundException extends SignUpClientException {

    public VerifyAccountNotFoundException() {
        super(SignUpErrorCode.NOT_VERIFICATION);
    }
}
