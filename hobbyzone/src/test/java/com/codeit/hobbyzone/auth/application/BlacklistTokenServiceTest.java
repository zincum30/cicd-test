package com.codeit.hobbyzone.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.codeit.hobbyzone.auth.domain.token.TokenType;
import com.codeit.hobbyzone.common.IntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BlacklistTokenServiceTest {

    @Autowired
    BlacklistTokenService blacklistTokenService;

    @Test
    void registerBlacklistToken_성공_테스트() {
        // given
        String accessToken = "Bearer accessToken";
        String refreshToken = "Bearer refreshToken";

        // when & then
        assertDoesNotThrow(() -> blacklistTokenService.registerBlacklistToken(accessToken, refreshToken));
    }

    @Test
    void existsBlackListToken_성공_테스트() {
        // when
        boolean actual = blacklistTokenService.existsBlacklistToken(TokenType.ACCESS, "accessToken");

        // then
        assertThat(actual).isFalse();
    }
}
