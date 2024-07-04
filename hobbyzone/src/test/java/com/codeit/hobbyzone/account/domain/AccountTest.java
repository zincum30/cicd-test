package com.codeit.hobbyzone.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AccountTest {

    @Test
    void 생성자_성공_테스트() {
        // given
        String email = "email@email.com";
        String password = "password";
        String nickname = "nickname";

        // when
        Account account = new Account(email, password, nickname);

        // then
        assertAll(
                () -> assertThat(account.getEmail()).isEqualTo(email),
                () -> assertThat(account.getPassword()).isEqualTo(password),
                () -> assertThat(account.getNickname()).isEqualTo(nickname)
        );
    }

    @Test
    void password_성공_테스트() {
        // given
        String email = "email@email.com";
        String password = "password";
        String name = "name";
        String nickname = "nickname";

        Account account = new Account(email, password, nickname);

        // when
        String actual = account.password();

        // then
        assertThat(actual).isEqualTo(password);
    }

    @Test
    void email_성공_테스트() {
        // given
        String email = "email@email.com";
        String password = "password";
        String nickname = "nickname";

        Account account = new Account(email, password, nickname);

        // when
        String actual = account.email();

        // then
        assertThat(actual).isEqualTo(email);
    }
}
