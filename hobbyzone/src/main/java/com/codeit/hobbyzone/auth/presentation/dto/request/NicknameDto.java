package com.codeit.hobbyzone.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NicknameDto(@NotBlank String nickname) {
}
