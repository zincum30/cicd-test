package com.codeit.hobbyzone.auth.infrastructure;

import com.codeit.hobbyzone.auth.domain.token.BlacklistToken;
import com.codeit.hobbyzone.auth.domain.token.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {

    boolean existsByTokenTypeAndToken(TokenType tokenType, String accessToken);
}
