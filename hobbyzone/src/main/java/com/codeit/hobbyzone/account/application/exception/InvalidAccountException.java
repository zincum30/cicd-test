package com.codeit.hobbyzone.account.application.exception;

import com.codeit.hobbyzone.common.exception.base.account.AccountClientException;
import com.codeit.hobbyzone.common.exception.code.AccountErrorCode;

public class InvalidAccountException extends AccountClientException {

    public InvalidAccountException() {
        super(AccountErrorCode.INVALID_ACCOUNT);
    }
}
