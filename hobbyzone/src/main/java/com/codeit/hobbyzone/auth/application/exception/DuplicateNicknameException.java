package com.codeit.hobbyzone.auth.application.exception;

import com.codeit.hobbyzone.common.exception.base.auth.SignUpClientException;
import com.codeit.hobbyzone.common.exception.code.SignUpErrorCode;

public class DuplicateNicknameException extends SignUpClientException {

    public DuplicateNicknameException() {
        super(SignUpErrorCode.DUPLICATE_NICKNAME);
    }
}
