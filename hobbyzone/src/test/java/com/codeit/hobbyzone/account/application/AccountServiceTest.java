package com.codeit.hobbyzone.account.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.codeit.hobbyzone.account.application.dto.request.ChangeAccountInfoDto;
import com.codeit.hobbyzone.account.application.dto.response.AccountInfoDto;
import com.codeit.hobbyzone.account.application.dto.response.AfterChangeAccountInfoDto;
import com.codeit.hobbyzone.account.application.exception.InvalidAccountException;
import com.codeit.hobbyzone.account.domain.Account;
import com.codeit.hobbyzone.account.domain.AccountBuilder;
import com.codeit.hobbyzone.account.infrastucture.AccountRepository;
import com.codeit.hobbyzone.common.IntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@IntegrationTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findAccountInfo_성공_테스트() {
        // given
        String email = "email@email.com";
        String nickname = "nickname";
        Account account = AccountBuilder.builder()
                                        .email(email)
                                        .password("password123")
                                        .nickname(nickname)
                                        .build();

        accountRepository.save(account);

        // when
        AccountInfoDto accountInfo = accountService.findAccountInfo(email);

        // then
        assertAll(
                () -> assertThat(accountInfo.email()).isEqualTo(email),
                () -> assertThat(accountInfo.nickname()).isEqualTo(nickname)
        );
    }

    @Test
    void findAccountInfo_실패_테스트_유효하지_않은_회원() {
        // when & then
        assertThatThrownBy(() -> accountService.findAccountInfo("invalid"))
                .isInstanceOf(InvalidAccountException.class);
    }

    @Test
    void withdrawal_성공_테스트() {
        // given
        String email = "email@email.com";
        Account account = AccountBuilder.builder()
                                        .email(email)
                                        .password("password123")
                                        .nickname("nickname")
                                        .build();

        accountRepository.save(account);

        // when
        accountService.withdrawal(email);

        // then
        assertThat(accountRepository.findByEmail(email)).isEmpty();
    }

    @Test
    void withdrawal_실패_테스트_유효하지_않은_회원() {
        // when & then
        assertThatThrownBy(() -> accountService.withdrawal("invalid"))
                .isInstanceOf(InvalidAccountException.class);
    }

    @Test
    void changeInfo_성공_테스트_프로필_이미지_포함() {
        // given
        String email = "email@email.com";
        Account account = AccountBuilder.builder()
                                        .email(email)
                                        .password("password123")
                                        .nickname("nickname")
                                        .build();

        accountRepository.save(account);

        MockMultipartFile profileImage = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        String changeNickname = "change";
        ChangeAccountInfoDto dto = new ChangeAccountInfoDto(email, profileImage, changeNickname);

        // when
        AfterChangeAccountInfoDto actual = accountService.changeAccountInfo(dto);

        // then
        assertAll(
                () -> assertThat(actual.email()).isEqualTo(account.email()),
                () -> assertThat(actual.nickname()).isEqualTo(account.getNickname()),
                () -> assertThat(actual.profileImageUrl()).isNotNull()
        );
    }

    @Test
    void changeInfo_성공_테스트_프로필_이미지_제외() {
        // given
        String email = "email@email.com";
        Account account = AccountBuilder.builder()
                                        .email(email)
                                        .password("password123")
                                        .nickname("nickname")
                                        .build();

        accountRepository.save(account);

        String changeNickname = "change";
        ChangeAccountInfoDto dto = new ChangeAccountInfoDto(email, null, changeNickname);

        // when
        AfterChangeAccountInfoDto actual = accountService.changeAccountInfo(dto);

        // then
        assertAll(
                () -> assertThat(actual.email()).isEqualTo(account.email()),
                () -> assertThat(actual.nickname()).isEqualTo(account.getNickname()),
                () -> assertThat(actual.profileImageUrl()).isNull()
        );
    }

    @Test
    void changeInfo_실패_테스트_유효하지_않은_회원() {
        // given
        ChangeAccountInfoDto invalidDto = new ChangeAccountInfoDto("invalid", null, "change");

        // when & then
        assertThatThrownBy(() -> accountService.changeAccountInfo(invalidDto))
                .isInstanceOf(InvalidAccountException.class);
    }
}
