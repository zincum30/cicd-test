package com.codeit.hobbyzone.auth.config.properties;

import com.codeit.hobbyzone.auth.domain.token.TokenType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("token")
public record JwtConfigurationProperties(
        String accessKey,
        String refreshKey,
        Long accessExpiredSeconds,
        Long refreshExpiredSeconds,
        Long accessExpiredMillisSeconds,
        Long refreshExpiredMillisSeconds
) {

    public String findTokenKey(TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessKey;
        }

        return refreshKey;
    }

    public Long findExpiredMillisSeconds(TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessExpiredMillisSeconds;
        }

        return refreshExpiredMillisSeconds;
    }
}
