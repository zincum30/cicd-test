package com.codeit.hobbyzone.auth.presentation;

import com.codeit.hobbyzone.auth.application.AuthService;
import com.codeit.hobbyzone.auth.application.dto.response.TokenDto;
import com.codeit.hobbyzone.auth.config.properties.JwtConfigurationProperties;
import com.codeit.hobbyzone.auth.presentation.dto.request.SignInDto;
import com.codeit.hobbyzone.auth.presentation.dto.response.AccessTokenDto;
import com.codeit.hobbyzone.auth.presentation.exception.RefreshTokenNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auths")
public class AuthController {

    private static final String REFRESH_TOKEN_KEY = "refreshToken";
    private static final String COOKIE_PATH = "/";

    private final AuthService authService;
    private final JwtConfigurationProperties jwtConfigurationProperties;

    @PostMapping("/signin")
    public ResponseEntity<AccessTokenDto> signIn(@RequestBody @Valid SignInDto dto) {
        TokenDto tokenDto = authService.signIn(dto.email(), dto.password());
        HttpCookie cookie = ResponseCookie.from(REFRESH_TOKEN_KEY, tokenDto.refreshToken())
                                          .httpOnly(true)
                                          .secure(true)
                                          .sameSite(SameSite.LAX.name())
                                          .maxAge(jwtConfigurationProperties.refreshExpiredSeconds())
                                          .path(COOKIE_PATH)
                                          .build();

        return ResponseEntity.ok()
                             .header(HttpHeaders.SET_COOKIE, cookie.toString())
                             .body(new AccessTokenDto(tokenDto.accessToken()));
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signOut(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            HttpServletRequest request
    ) {
        findRefreshToken(request.getCookies())
                .ifPresent(refreshToken -> authService.signOut(accessToken, refreshToken));

        return ResponseEntity.noContent()
                             .build();
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<AccessTokenDto> refreshToken(HttpServletRequest request) {
        String refreshToken = findRefreshToken(request.getCookies()).orElseThrow(RefreshTokenNotFoundException::new);
        TokenDto tokenDto = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(new AccessTokenDto(tokenDto.accessToken()));
    }

    private Optional<String> findRefreshToken(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (REFRESH_TOKEN_KEY.equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }

        return Optional.empty();
    }
}
