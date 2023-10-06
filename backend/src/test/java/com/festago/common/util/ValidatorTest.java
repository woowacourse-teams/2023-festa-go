package com.festago.common.util;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ValidatorTest {

    @Test
    void 문자열_maxLength_검증_성공() {
        // given
        String input = "1234567890"; // 10

        // when & then
        assertThatNoException()
            .isThrownBy(() -> Validator.maxLength(input, 10, ""));
    }

    @Test
    void 문자열_maxLength_검증_실패() {
        // given
        String input = "12345678901"; // 11

        // when & then
        assertThatThrownBy(() -> Validator.maxLength(input, 10, ""))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 문자열_maxLength_검증_null_성공() {
        // when & then
        assertThatNoException()
            .isThrownBy(() -> Validator.maxLength(null, 10, ""));
    }

    @Test
    void 문자열_minLength_검증_성공() {
        // given
        String input = "1234567890"; // 10

        // when & then
        assertThatNoException()
            .isThrownBy(() -> Validator.minLength(input, 10, ""));
    }

    @Test
    void 문자열_minLength_검증_실패() {
        // given
        String input = "123456789"; // 9

        // when & then
        assertThatThrownBy(() -> Validator.minLength(input, 10, ""))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 문자열_minLength_검증_null_성공() {
        // when & then
        assertThatNoException()
            .isThrownBy(() -> Validator.minLength(null, 1, ""));
    }
}
