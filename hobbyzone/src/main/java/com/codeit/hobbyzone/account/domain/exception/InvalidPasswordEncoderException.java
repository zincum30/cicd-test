package com.codeit.hobbyzone.account.domain.exception;

import com.codeit.hobbyzone.common.exception.base.account.AccountServerException;
import com.codeit.hobbyzone.common.exception.code.AccountErrorCode;

public class InvalidPasswordEncoderException extends AccountServerException {

    public InvalidPasswordEncoderException() {
        super(AccountErrorCode.SERVER_ERROR);
    }
}
