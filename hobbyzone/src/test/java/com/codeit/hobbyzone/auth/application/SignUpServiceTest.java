package com.codeit.hobbyzone.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.codeit.hobbyzone.account.presentation.AccountRepository;
import com.codeit.hobbyzone.auth.application.dto.request.SignUpInfoDto;
import com.codeit.hobbyzone.auth.application.event.SentMailEvent;
import com.codeit.hobbyzone.auth.application.exception.NotVerifyAccountException;
import com.codeit.hobbyzone.auth.application.exception.VerifyAccountNotFoundException;
import com.codeit.hobbyzone.auth.domain.VerifyAccount;
import com.codeit.hobbyzone.auth.domain.exception.ExpiredVerifyCodeException;
import com.codeit.hobbyzone.auth.domain.exception.VerifyFailedException;
import com.codeit.hobbyzone.auth.domain.utils.VerifyCodeHolder;
import com.codeit.hobbyzone.auth.infrastructure.VerifyAccountRepository;
import com.codeit.hobbyzone.common.IntegrationTest;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@IntegrationTest
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SignUpServiceTest {

    @MockBean
    VerifyCodeHolder verifyCodeHolder;

    @MockBean
    MailSender mailSender;

    @MockBean
    Clock clock;

    @Autowired
    SignUpService signUpService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    VerifyAccountRepository verifyAccountRepository;

    @Autowired
    ApplicationEvents events;

    @Test
    void signUp_메서드_성공_테스트() {
        // given
        String email = "email@email.com";
        String verifyCode = "12345678";
        Clock clock = Clock.systemDefaultZone();
        VerifyAccount verifyAccount = new VerifyAccount(email, verifyCode, clock);

        verifyAccount.verify(verifyCode, clock);
        verifyAccountRepository.save(verifyAccount);

        SignUpInfoDto dto = new SignUpInfoDto(email, "password123", "nickname");

        // when
        Long accountId = signUpService.signUp(dto);

        // then
        assertThat(accountId).isPositive();
    }

    @Test
    void signUp_메서드_실패_테스트_메일_인증_요청을_하지_않은_회원() {
        // given
        SignUpInfoDto dto = new SignUpInfoDto("email@email.com", "password123", "nickname");

        // when & then
        assertThatThrownBy(() -> signUpService.signUp(dto)).isInstanceOf(VerifyAccountNotFoundException.class);
    }

    @Test
    void signUp_메서드_실패_테스트_메일_인증을_하지_않은_회원() {
        // given
        String email = "email@email.com";
        SignUpInfoDto dto = new SignUpInfoDto("email@email.com", "password123", "nickname");
        VerifyAccount verifyAccount = new VerifyAccount(email, "12345678", Clock.systemDefaultZone());

        verifyAccountRepository.save(verifyAccount);

        // when & then
        assertThatThrownBy(() -> signUpService.signUp(dto)).isInstanceOf(NotVerifyAccountException.class);
    }

    @Test
    void sendMail_메서드_성공_테스트() {
        // given
        Instant instant = LocalDateTime.now().atOffset(ZoneOffset.ofHours(0)).toInstant();

        given(clock.instant()).willReturn(instant);
        given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));

        // when
        String email = "email@email.com";
        signUpService.sendMail(email);

        // then
        VerifyAccount actual = verifyAccountRepository.findByEmail(email)
                                                      .get();

        assertAll(
                () -> assertThat(actual.getEmail()).isEqualTo("email@email.com"),
                () -> assertThat(events.stream(SentMailEvent.class).count()).isOne()
        );
    }

    @Test
    void verify_메서드_성공_테스트() {
        // given
        String email = "email@email.com";
        String verifyCode = "12345678";
        Clock clock = Clock.systemDefaultZone();
        VerifyAccount verifyAccount = new VerifyAccount(email, verifyCode, clock);

        verifyAccountRepository.save(verifyAccount);

        // when
        verifyAccount.verify(verifyCode, clock);

        // then
        assertThat(verifyAccount.isEmailVerified()).isTrue();
    }

    @Test
    void verify_메서드_실패_테스트_인증_코드_불일치() {
        // given
        String email = "email@email.com";
        Clock verifyClock = Clock.systemDefaultZone();
        VerifyAccount verifyAccount = new VerifyAccount(email, "12345678", verifyClock);

        verifyAccountRepository.save(verifyAccount);

        Instant instant = LocalDateTime.now(verifyClock).atOffset(ZoneOffset.ofHours(0)).toInstant();

        given(clock.instant()).willReturn(instant);
        given(clock.getZone()).willReturn(ZoneOffset.ofHours(0));

        // when & then
        assertThatThrownBy(() -> signUpService.verify(email, "invalid"))
                .isInstanceOf(VerifyFailedException.class);
    }

    @Test
    void verify_메서드_실패_테스트_인증_코드_만료() {
        // given
        String email = "email@email.com";
        Clock verifyClock = Clock.systemDefaultZone();
        VerifyAccount verifyAccount = new VerifyAccount(email, "12345678", verifyClock);
        Instant instant = LocalDateTime.now(verifyClock).atOffset(ZoneOffset.ofHours(-10)).toInstant();

        verifyAccountRepository.save(verifyAccount);

        given(clock.instant()).willReturn(instant);
        given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));

        // when & then
        assertThatThrownBy(() -> signUpService.verify(email, "12345678"))
                .isInstanceOf(ExpiredVerifyCodeException.class);
    }

    @Test
    void verify_메서드_실패_인증_메일_요청_안함() {
        // when & then
        assertThatThrownBy(() -> signUpService.verify("email@email.com", "12345678"))
                .isInstanceOf(VerifyAccountNotFoundException.class);
    }

    @Test
    void isDuplicateNickname_성공_테스트() {
        // when
        boolean actual = signUpService.isDuplicateNickname("nickname");

        // then
        assertThat(actual).isFalse();
    }
}
