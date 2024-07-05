package com.codeit.hobbyzone.auth.infrastructure.exception;

import com.codeit.hobbyzone.common.exception.base.auth.SignUpServerException;
import com.codeit.hobbyzone.common.exception.code.SignUpErrorCode;

public class MailSendFailedException extends SignUpServerException {

    public MailSendFailedException() {
        super(SignUpErrorCode.SERVER_ERROR);
    }
}
