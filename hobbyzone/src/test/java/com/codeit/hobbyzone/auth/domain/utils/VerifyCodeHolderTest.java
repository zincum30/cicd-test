package com.codeit.hobbyzone.auth.domain.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.RepeatedTest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class VerifyCodeHolderTest {

    @RepeatedTest(10)
    void generate_성공_테스트() {
        // given
        VerifyCodeHolder verifyCodeHolder = new VerifyCodeHolder();

        // when
        String actual = verifyCodeHolder.generate();

        // then
        assertThat(actual.length()).isEqualTo(6);
    }
}
