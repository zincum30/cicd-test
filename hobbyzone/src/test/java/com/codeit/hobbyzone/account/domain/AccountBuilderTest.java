package com.codeit.hobbyzone.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.codeit.hobbyzone.account.domain.exception.InvalidNicknameException;
import com.codeit.hobbyzone.account.domain.exception.InvalidPasswordEncoderException;
import com.codeit.hobbyzone.account.domain.exception.InvalidPasswordException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AccountBuilderTest {

    AccountBuilder builder = AccountBuilder.builder();

    @Test
    void build_성공_테스트() {
        // given
        String email = "email@email.com";
        String password = "password123";
        String nickname = "nickname";

        // when
        Account actual = AccountBuilder.builder()
                                       .email(email)
                                       .password(password)
                                       .nickname(nickname)
                                       .build();

        // then
        assertAll(
                () -> assertThat(actual.getEmail()).isEqualTo(email),
                () -> assertThat(actual.getPassword()).isNotEqualTo(password),
                () -> assertThat(actual.getNickname()).isEqualTo(nickname)
        );
    }

    @ParameterizedTest(name = "{0} 일 때 InvalidPasswordException이 발생한다")
    @CsvSource(
            value = {
                    "null", "''", "1ab2", "123456789", "12345678", "abcdefgh"
            },
            delimiter = ':',
            nullValues = "null"
    )
    void builder_실패_테스트_유효하지_않은_비밀번호(String invalidPassword) {
        // when & then
        assertThatThrownBy(() -> builder.password(invalidPassword)).isInstanceOf(InvalidPasswordException.class);
    }

    @ParameterizedTest(name = "{0} 일 때 InvalidNicknameException이 발생한다")
    @CsvSource(
            value = {
                    "null", "''", "1", "123456789"
            },
            delimiter = ':',
            nullValues = "null"
    )
    void build_실패_테스트_유효하지_않은_닉네임(String invalidNickname) {
        // when & then
        assertThatThrownBy(() -> builder.nickname(invalidNickname)).isInstanceOf(InvalidNicknameException.class);
    }

    @ParameterizedTest(name = "{0} 일 때 InvalidPasswordEncoderException이 발생한다")
    @NullSource
    void build_실패_테스트_유효하지_않은_passwordEncoder(PasswordEncoder invalidEncoder) {
        // when & then
        assertThatThrownBy(() -> builder.passwordEncoder(invalidEncoder))
                .isInstanceOf(InvalidPasswordEncoderException.class);
    }
}
