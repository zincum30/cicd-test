package com.codeit.hobbyzone.auth.domain.token;

import com.codeit.hobbyzone.auth.domain.exception.EmptyTokenException;
import com.codeit.hobbyzone.common.domain.CreateTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, of = "blacklistTokenId")
public class BlacklistToken extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blacklistTokenId;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private String token;

    public BlacklistToken(TokenType tokenType, String token) {
        validateToken(token);

        this.tokenType = tokenType;
        this.token = token;
    }

    private void validateToken(String targetToken) {
        if (targetToken == null || targetToken.isBlank()) {
            throw new EmptyTokenException();
        }
    }
}
