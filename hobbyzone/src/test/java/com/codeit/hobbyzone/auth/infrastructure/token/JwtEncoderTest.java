package com.codeit.hobbyzone.auth.infrastructure.token;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeit.hobbyzone.auth.config.properties.JwtConfigurationProperties;
import com.codeit.hobbyzone.auth.domain.token.TokenType;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtEncoderTest {

    JwtConfigurationProperties jwtConfigProperties = new JwtConfigurationProperties(
            "thisistoolargeaccesstokenkeyfordummykeydatafortest",
            "thisistoolargerefreshtokenkeyfordummykeydatafortest",
            43200L, 259200L,
            43200000L, 259200000L
    );
    JwtEncoder jwtEncoder = new JwtEncoder(jwtConfigProperties);

    @Test
    void encode_성공_테스트() {
        // when
        String actual = jwtEncoder.encode(LocalDateTime.now(), TokenType.ACCESS, Map.of("accountId", 1L));

        // then
        assertThat(actual).isNotBlank();
    }
}
