package com.codeit.hobbyzone.auth.application.dto.response;

import com.codeit.hobbyzone.auth.domain.token.TokenScheme;

public record TokenDto(String accessToken, String refreshToken, TokenScheme tokenScheme) {
}
