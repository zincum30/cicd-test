package com.codeit.hobbyzone.auth.domain.exception;

import com.codeit.hobbyzone.common.exception.base.auth.SignUpClientException;
import com.codeit.hobbyzone.common.exception.code.SignUpErrorCode;

public class ExpiredVerifyCodeException extends SignUpClientException {

    public ExpiredVerifyCodeException() {
        super(SignUpErrorCode.EXPIRED_VERIFY_CODE);
    }
}
