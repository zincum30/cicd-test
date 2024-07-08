package com.codeit.hobbyzone.image.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProfileImageTest {

    @Test
    void 생성자_성공_테스트() {
        // when & then
        assertDoesNotThrow(() -> new ProfileImage("uploadName", "storeName"));
    }
}
