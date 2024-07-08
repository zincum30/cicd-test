package com.codeit.hobbyzone.auth.domain.token;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.codeit.hobbyzone.auth.domain.exception.EmptyTokenException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BlackListTokenTest {

    @Test
    void 생성자_성공_테스트() {
        // when & then
        assertDoesNotThrow(() -> new BlacklistToken(TokenType.ACCESS, "accessToken"));
    }

    @ParameterizedTest(name = "{0}일 때 EmptyTokenException 예외가 발생한다")
    @ValueSource(strings = {"       ", ""})
    void 생성자_실패_테스트(String invalidToken) {
        // when & then
        assertThatThrownBy(() -> new BlacklistToken(TokenType.ACCESS, invalidToken))
                .isInstanceOf(EmptyTokenException.class);
    }
}
