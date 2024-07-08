package com.codeit.hobbyzone.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeit.hobbyzone.account.domain.Account;
import com.codeit.hobbyzone.account.domain.AccountBuilder;
import com.codeit.hobbyzone.account.presentation.AccountRepository;
import com.codeit.hobbyzone.common.IntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ValidateAccountServiceTest {

    @Autowired
    ValidateAccountService validateAccountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void isValidAccount_성공_테스트_존재하지_않는_회원() {
        // when
        boolean actual = validateAccountService.isValidAccount("invalid email");

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void isValidAccount_성공_테스트_탈퇴한_회원() {
        // given
        String email = "email@email.com";
        Account account = AccountBuilder.builder()
                                        .email(email)
                                        .password("password123")
                                        .nickname("nickname")
                                        .build();

        account.withdrawal();
        accountRepository.save(account);

        // when
        boolean actual = validateAccountService.isValidAccount(email);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void isValidAccount_성공_유효한_회원() {
        // given
        Account account = AccountBuilder.builder()
                                        .email("email@email.com")
                                        .password("password123")
                                        .nickname("nickname")
                                        .build();

        accountRepository.save(account);

        // when
        boolean actual = validateAccountService.isValidAccount(account.email());

        // then
        assertThat(actual).isTrue();
    }
}
