package com.codeit.hobbyzone.common.exception.base.account;

import com.codeit.hobbyzone.common.exception.code.AccountErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountServerException extends IllegalArgumentException {

    private final AccountErrorCode errorCode;
}
