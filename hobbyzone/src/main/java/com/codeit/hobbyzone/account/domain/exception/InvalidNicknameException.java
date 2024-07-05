package com.codeit.hobbyzone.account.domain.exception;

import com.codeit.hobbyzone.common.exception.base.account.AccountClientException;
import com.codeit.hobbyzone.common.exception.code.AccountErrorCode;

public class InvalidNicknameException extends AccountClientException {

    public InvalidNicknameException() {
        super(AccountErrorCode.VALIDATION_NICKNAME_ERROR);
    }
}
