package com.codeit.hobbyzone.account.presentation.dto.response;

import com.codeit.hobbyzone.account.application.dto.response.AccountInfoDto;

public record ReadAccountInfoDto(String email, String nickname, String profileImageUrl) {

    public static ReadAccountInfoDto from(AccountInfoDto dto) {
        return new ReadAccountInfoDto(dto.email(), dto.nickname(), dto.profileImageUrl());
    }
}
