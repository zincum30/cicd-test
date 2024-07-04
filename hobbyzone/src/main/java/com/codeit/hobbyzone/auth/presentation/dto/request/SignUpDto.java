package com.codeit.hobbyzone.auth.presentation.dto.request;

import com.codeit.hobbyzone.auth.application.dto.request.SignUpInfoDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpDto(
        @Email
        String email,

        @NotBlank
        String password,

        @NotBlank
        String nickname
) {

    public SignUpInfoDto convert() {
        return new SignUpInfoDto(email, password, nickname);
    }
}
