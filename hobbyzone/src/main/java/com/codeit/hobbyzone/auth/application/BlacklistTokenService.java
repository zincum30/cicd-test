package com.codeit.hobbyzone.auth.application;

import com.codeit.hobbyzone.auth.domain.token.BlacklistToken;
import com.codeit.hobbyzone.auth.domain.token.TokenDecoder;
import com.codeit.hobbyzone.auth.domain.token.TokenType;
import com.codeit.hobbyzone.auth.infrastructure.BlacklistTokenRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistTokenService {

    private final BlacklistTokenRepository blacklistTokenRepository;
    private final TokenDecoder tokenDecoder;

    public void registerBlacklistToken(String accessToken, String refreshToken) {
        List<BlacklistToken> blackListTokens = new ArrayList<>();

        if (isValidToken(TokenType.ACCESS, accessToken)) {
            blackListTokens.add(new BlacklistToken(TokenType.ACCESS, accessToken));
        }
        if (isValidToken(TokenType.REFRESH, refreshToken)) {
            blackListTokens.add(new BlacklistToken(TokenType.REFRESH, refreshToken));
        }

        blacklistTokenRepository.saveAll(blackListTokens);
    }

    public boolean existsBlacklistToken(TokenType tokenType, String targetToken) {
        return blacklistTokenRepository.existsByTokenTypeAndToken(tokenType, targetToken);
    }

    private boolean isValidToken(TokenType tokenType, String targetToken) {
        return tokenDecoder.decode(tokenType, targetToken)
                           .isPresent();
    }
}
