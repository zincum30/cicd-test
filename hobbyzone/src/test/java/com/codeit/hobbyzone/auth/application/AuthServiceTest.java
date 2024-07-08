package com.codeit.hobbyzone.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.codeit.hobbyzone.account.domain.Account;
import com.codeit.hobbyzone.account.domain.AccountBuilder;
import com.codeit.hobbyzone.account.infrastucture.AccountRepository;
import com.codeit.hobbyzone.auth.application.dto.response.TokenDto;
import com.codeit.hobbyzone.auth.application.exception.UnregisteredAccountException;
import com.codeit.hobbyzone.auth.domain.token.TokenScheme;
import com.codeit.hobbyzone.auth.domain.token.TokenType;
import com.codeit.hobbyzone.auth.infrastructure.exception.InvalidTokenException;
import com.codeit.hobbyzone.auth.infrastructure.token.JwtEncoder;
import com.codeit.hobbyzone.common.IntegrationTest;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JwtEncoder jwtEncoder;

    @Test
    void signIn_성공_테스트() {
        // given
        String email = "email@email.com";
        String password = "password123";
        Account account = AccountBuilder.builder()
                                        .email(email)
                                        .password(password)
                                        .nickname("nickname")
                                        .build();

        accountRepository.save(account);

        // when
        TokenDto actual = authService.signIn(email, password);

        assertAll(
                () -> assertThat(actual.accessToken()).isNotBlank(),
                () -> assertThat(actual.refreshToken()).isNotBlank(),
                () -> assertThat(actual.tokenScheme()).isEqualTo(TokenScheme.BEARER)
        );
    }

    @Test
    void signIn_실패_테스트_가입하지_않은_이메일() {
        // when & then
        assertThatThrownBy(() -> authService.signIn("email@email.com", "password123"))
                .isInstanceOf(UnregisteredAccountException.class);
    }

    @Test
    void signOut_성공_테스트() {
        // when & then
        assertDoesNotThrow(() -> authService.signOut("Bearer accessToken", "Bearer refreshToken"));
    }

    @Test
    void refreshToken_성공_테스트() {
        // given
        String refreshToken = "Bearer " + jwtEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of("email", "email@email.com")
        );

        // when
        TokenDto actual = authService.refreshToken(refreshToken);

        // then
        assertAll(
                () -> assertThat(actual.accessToken()).isNotBlank(),
                () -> assertThat(actual.refreshToken()).isNotBlank(),
                () -> assertThat(actual.tokenScheme()).isEqualTo(TokenScheme.BEARER)
        );
    }

    @Test
    void refreshToken_실패_테스트_유효하지_않은_refreshToken() {
        // when & then
        assertThatThrownBy(() -> authService.refreshToken("invalid")).isInstanceOf(InvalidTokenException.class);
    }
}
