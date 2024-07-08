package com.codeit.hobbyzone.auth.application;

import com.codeit.hobbyzone.account.domain.Account;
import com.codeit.hobbyzone.account.presentation.AccountRepository;
import com.codeit.hobbyzone.auth.application.dto.response.TokenDto;
import com.codeit.hobbyzone.auth.application.exception.PasswordMismatchException;
import com.codeit.hobbyzone.auth.application.exception.UnregisteredAccountException;
import com.codeit.hobbyzone.auth.domain.token.PrivateClaims;
import com.codeit.hobbyzone.auth.domain.token.TokenDecoder;
import com.codeit.hobbyzone.auth.domain.token.TokenEncoder;
import com.codeit.hobbyzone.auth.domain.token.TokenScheme;
import com.codeit.hobbyzone.auth.domain.token.TokenType;
import com.codeit.hobbyzone.auth.infrastructure.exception.InvalidTokenException;
import com.codeit.hobbyzone.auth.infrastructure.token.JwtDecoder;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final BlacklistTokenService blacklistTokenService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenEncoder tokenEncoder;
    private final TokenDecoder tokenDecoder;

    public TokenDto signIn(String email, String password) {
        Account account = accountRepository.findByEmail(email)
                                           .orElseThrow(UnregisteredAccountException::new);

        if (!passwordEncoder.matches(password, account.password())) {
            throw new PasswordMismatchException();
        }

        String accessToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of(JwtDecoder.CLAIM_NAME, account.getAccountId())
        );
        String refreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of(JwtDecoder.CLAIM_NAME, account.getAccountId())
        );

        return new TokenDto(accessToken, refreshToken, TokenScheme.BEARER);
    }

    public void signOut(String accessToken, String refreshToken) {
        blacklistTokenService.registerBlacklistToken(accessToken, refreshToken);
    }

    public TokenDto refreshToken(String refreshToken) {
        PrivateClaims privateClaims = tokenDecoder.decode(TokenType.REFRESH, refreshToken)
                                                  .orElseThrow(InvalidTokenException::new);
        String accessToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of(JwtDecoder.CLAIM_NAME, privateClaims.email())
        );

        return new TokenDto(accessToken, refreshToken, TokenScheme.BEARER);
    }
}
