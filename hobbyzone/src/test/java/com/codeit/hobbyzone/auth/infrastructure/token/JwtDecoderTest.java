package com.codeit.hobbyzone.auth.infrastructure.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.codeit.hobbyzone.auth.config.properties.JwtConfigurationProperties;
import com.codeit.hobbyzone.auth.domain.token.PrivateClaims;
import com.codeit.hobbyzone.auth.domain.token.TokenType;
import com.codeit.hobbyzone.auth.infrastructure.exception.InvalidTokenException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtDecoderTest {

    JwtConfigurationProperties jwtConfigProperties = new JwtConfigurationProperties(
            "thisistoolargeaccesstokenkeyfordummykeydatafortest",
            "thisistoolargerefreshtokenkeyfordummykeydatafortest",
            43200L, 259200L,
            43200000L, 259200000L
    );
    JwtDecoder jwtDecoder = new JwtDecoder(jwtConfigProperties);

    @Test
    void decode_성공_유효하지_않은_PrivateClaims() {
        // given
        String invalidToken = "Bearer abcde";

        // when
        Optional<PrivateClaims> actual = jwtDecoder.decode(TokenType.ACCESS, invalidToken);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void decode_성공_테스트_유효한_PrivateClaims() {
        // given
        JwtEncoder jwtEncoder = new JwtEncoder(jwtConfigProperties);
        String claimKey = "email";
        Map<String, Object> claims = Map.of(claimKey, "email@email.com");
        String validToken = "Bearer " + jwtEncoder.encode(LocalDateTime.now(), TokenType.ACCESS, claims);

        // when
        Optional<PrivateClaims> actual = jwtDecoder.decode(TokenType.ACCESS, validToken);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().email()).isEqualTo(claims.get(claimKey))
        );
    }

    @Test
    void decode_실패_테스트_유효하지_않은_토큰_길이() {
        // given
        String invalidToken = "abcde";

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(TokenType.ACCESS, invalidToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void decode_실패_테스트_유효하지_않은_토큰_타입() {
        // given
        String invalidToken = "Basic12 abcde";

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(TokenType.ACCESS, invalidToken))
                .isInstanceOf(InvalidTokenException.class);
    }
}
