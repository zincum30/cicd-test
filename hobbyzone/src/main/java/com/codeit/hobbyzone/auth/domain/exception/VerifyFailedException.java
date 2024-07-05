package com.codeit.hobbyzone.auth.domain.exception;

import com.codeit.hobbyzone.common.exception.base.auth.SignUpClientException;
import com.codeit.hobbyzone.common.exception.code.SignUpErrorCode;

public class VerifyFailedException extends SignUpClientException {

    public VerifyFailedException() {
        super(SignUpErrorCode.VERIFICATION_FAILED);
    }
}
