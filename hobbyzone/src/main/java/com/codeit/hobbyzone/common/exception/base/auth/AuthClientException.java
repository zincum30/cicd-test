package com.codeit.hobbyzone.common.exception.base.auth;

import com.codeit.hobbyzone.common.exception.code.AuthErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthClientException extends IllegalArgumentException {

    private final AuthErrorCode errorCode;

}
