package com.codeit.hobbyzone.auth.config.interceptor;

import com.codeit.hobbyzone.auth.application.BlacklistTokenService;
import com.codeit.hobbyzone.auth.application.ValidateAccountService;
import com.codeit.hobbyzone.auth.domain.token.PrivateClaims;
import com.codeit.hobbyzone.auth.domain.token.TokenDecoder;
import com.codeit.hobbyzone.auth.domain.token.TokenType;
import com.codeit.hobbyzone.auth.infrastructure.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final BlacklistTokenService blacklistTokenService;
    private final ValidateAccountService validateAccountService;
    private final TokenDecoder tokenDecoder;
    private final AuthStore store;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isNotRequiredAuthenticate(accessToken)) {
            store.set(new AuthAccountInfo(null));
            return true;
        }

        validateLogoutToken(accessToken);

        PrivateClaims privateClaims = tokenDecoder.decode(TokenType.ACCESS, accessToken)
                                                  .orElseThrow(InvalidTokenException::new);

        if (validateAccountService.isValidAccount(privateClaims.email())) {
            throw new InvalidTokenException();
        }

        store.set(new AuthAccountInfo(privateClaims.email()));
        return true;
    }

    private boolean isNotRequiredAuthenticate(String token) {
        return token == null || token.length() == 0;
    }

    private void validateLogoutToken(String accessToken) {
        if (blacklistTokenService.existsBlacklistToken(TokenType.ACCESS, accessToken)) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest ignoreRequest,
            HttpServletResponse ignoreResponse,
            Object ignoreHandler,
            Exception ignoreEx
    ) {
        store.remove();
    }
}
