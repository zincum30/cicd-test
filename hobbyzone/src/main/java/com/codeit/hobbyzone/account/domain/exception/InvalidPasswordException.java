package com.codeit.hobbyzone.account.domain.exception;

import com.codeit.hobbyzone.common.exception.base.account.AccountClientException;
import com.codeit.hobbyzone.common.exception.code.AccountErrorCode;

public class InvalidPasswordException extends AccountClientException {

    public InvalidPasswordException() {
        super(AccountErrorCode.VALIDATION_PASSWORD_ERROR);
    }
}
