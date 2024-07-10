package com.codeit.hobbyzone.auth.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyDto(
        @Email
        String email,

        @NotBlank
        String code
) {
}