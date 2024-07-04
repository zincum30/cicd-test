package com.codeit.hobbyzone.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.codeit.hobbyzone.auth.domain.exception.ExpiredVerifyCodeException;
import com.codeit.hobbyzone.auth.domain.exception.VerifyFailedException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class VerifyAccountTest {

    @Test
    void 생성자_성공_테스트() {
        // given
        String email = "email@email.com";
        String code = "12345678";
        Clock clock = Clock.systemDefaultZone();

        // when
        VerifyAccount actual = new VerifyAccount(email, code, clock);

        // then
        assertAll(
                () -> assertThat(actual.getEmail()).isEqualTo(email),
                () -> assertThat(actual.getVerifyCode()).isEqualTo(code),
                () -> assertThat(actual.getExpiredAt().truncatedTo(ChronoUnit.SECONDS))
                        .isEqualTo(LocalDateTime.now(clock).plusMinutes(5L).truncatedTo(ChronoUnit.SECONDS))
        );
    }

    @Test
    void verify_성공_테스트() {
        // given
        String code = "12345678";
        Clock clock = Clock.systemDefaultZone();
        VerifyAccount verifyAccount = new VerifyAccount("email@email.com", code, clock);

        // when
        verifyAccount.verify(code, clock);

        // then
        assertThat(verifyAccount.isEmailVerified()).isTrue();
    }

    @Test
    void verify_실패_테스트_인증_코드_불일치() {
        // given
        Clock clock = Clock.systemDefaultZone();
        VerifyAccount verifyAccount = new VerifyAccount("email@email.com", "12345678", clock);

        // when & then
        assertThatThrownBy(() -> verifyAccount.verify("invalid", clock))
                .isInstanceOf(VerifyFailedException.class);
    }

    @Test
    void verify_실패_테스트_만료_인증_코드() {
        // given
        String verifyCode = "12345678";
        Clock clock = Clock.systemDefaultZone();
        VerifyAccount verifyAccount = new VerifyAccount("email@email.com", verifyCode, clock);

        Instant expiredInstant = LocalDateTime.now(clock)
                                              .atOffset(ZoneOffset.ofHoursMinutes(0, 5))
                                              .toInstant();
        Clock expiredClock = Clock.fixed(expiredInstant, ZoneId.systemDefault());

        // when & then
        assertThatThrownBy(() -> verifyAccount.verify(verifyCode, expiredClock))
                .isInstanceOf(ExpiredVerifyCodeException.class);
    }
}
