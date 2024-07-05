package com.codeit.hobbyzone.common.exception.base.auth;

import com.codeit.hobbyzone.common.exception.code.SignUpErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class SignUpClientException extends IllegalStateException {

    private final SignUpErrorCode errorCode;
}
