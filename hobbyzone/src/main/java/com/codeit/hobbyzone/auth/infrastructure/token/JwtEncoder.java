package com.codeit.hobbyzone.auth.infrastructure.token;

import com.codeit.hobbyzone.auth.config.properties.JwtConfigurationProperties;
import com.codeit.hobbyzone.auth.domain.token.TokenEncoder;
import com.codeit.hobbyzone.auth.domain.token.TokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtEncoder implements TokenEncoder {

    private final JwtConfigurationProperties jwtConfigurationProperties;

    @Override
    public String encode(LocalDateTime publishTime, TokenType tokenType, Map<String, Object> privateClaims) {
        Date targetDate = convertDate(publishTime);
        String key = jwtConfigurationProperties.findTokenKey(tokenType);
        Long expiredMillisSeconds = jwtConfigurationProperties.findExpiredMillisSeconds(tokenType);

        return Jwts.builder()
                   .setIssuedAt(targetDate)
                   .setExpiration(new Date(targetDate.getTime() + expiredMillisSeconds))
                   .addClaims(privateClaims)
                   .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                   .compact();
    }

    private Date convertDate(LocalDateTime targetTime) {
        Instant targetInstant = targetTime.atZone(ZoneId.of("Asia/Seoul"))
                                          .toInstant();

        return Date.from(targetInstant);
    }
}
