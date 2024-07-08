package com.codeit.hobbyzone.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.codeit.hobbyzone.account.domain.exception.InvalidNicknameException;
import com.codeit.hobbyzone.image.domain.ProfileImage;
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

    @Test
    void withdrawal_성공_테스트() {
        // given
        String email = "email@email.com";
        String password = "password123";
        String nickname = "nickname";

        Account account = new Account(email, password, nickname);

        // when
        account.withdrawal();

        // then
        assertAll(
                () -> assertThat(account.isWithdrawal()).isTrue(),
                () -> assertThat(account.getEmail()).isNull(),
                () -> assertThat(account.getPassword()).isNull()
        );

        boolean actual = account.isWithdrawal();

        assertThat(actual).isTrue();
    }

    @Test
    void changeNickname_성공_메서드() {
        // given
        String email = "email@email.com";
        String password = "password123";
        String nickname = "nickname";

        Account account = new Account(email, password, nickname);

        String changeNickname = "change";

        // when
        account.changeNickname(changeNickname);

        // then
        assertThat(account.getNickname()).isEqualTo(changeNickname);
    }

    @Test
    void changeInfo_실패_테스트_유효하지_않은_닉네임() {
        // given
        String email = "email@email.com";
        String password = "password123";
        String nickname = "nickname";

        Account account = new Account(email, password, nickname);

        // when & then
        assertThatThrownBy(() -> account.changeNickname(""))
                .isInstanceOf(InvalidNicknameException.class);
    }

    @Test
    void changeProfileImage_성공_테스트() {
        // given
        String email = "email@email.com";
        String password = "password123";
        String nickname = "nickname";

        Account account = new Account(email, password, nickname);

        // when
        account.changeProfileImage(new ProfileImage("uploadName", "storeName"));

        // then
        assertThat(account.getProfileImage()).isNotNull();
    }
}
