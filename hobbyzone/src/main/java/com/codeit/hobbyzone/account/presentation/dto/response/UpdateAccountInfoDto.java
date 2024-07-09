package com.codeit.hobbyzone.account.presentation.dto.response;

import com.codeit.hobbyzone.account.application.dto.response.AfterChangeAccountInfoDto;

public record UpdateAccountInfoDto(String email, String nickname, String profileImageUrl) {

    public static UpdateAccountInfoDto from(AfterChangeAccountInfoDto dto) {
        return new UpdateAccountInfoDto(dto.email(), dto.nickname(), dto.profileImageUrl());
    }
}
