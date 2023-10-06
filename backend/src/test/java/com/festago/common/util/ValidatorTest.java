package com.festago.common.util;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.ValidException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ValidatorTest {

    @Nested
    class hasBlank {

        @ParameterizedTest
        @NullSource
        void 문자열이_null이면_예외(String input) {
            // when & then
            assertThatThrownBy(() -> Validator.hasBlank(input, ""))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void 문자열이_공백이면_예외(String input) {
            // when & then
            assertThatThrownBy(() -> Validator.hasBlank(input, ""))
                .isInstanceOf(ValidException.class);
        }

        @Test
        void 문자열이_공백이_아니면_통과() {
            // given
            String input = "1";

            // when & then
            assertThatNoException()
                .isThrownBy(() -> Validator.hasBlank(input, ""));
        }
    }


    @Nested
    class maxLength {

        @Test
        void 문자열의_길이가_10이고_최대_길이가_10이면_통과() {
            // given
            String input = "1234567890"; // 10
            int maxLength = 10;

            // when & then
            assertThatNoException()
                .isThrownBy(() -> Validator.maxLength(input, maxLength, ""));
        }

        @Test
        void 문자열의_길이가_11이고_최대_길이가_10이면_예외() {
            // given
            String input = "12345678901"; // 11
            int maxLength = 10;

            // when & then
            assertThatThrownBy(() -> Validator.maxLength(input, maxLength, ""))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void 최대_길이가_0이하면_IllegalArgumentException(int maxLength) {
            // given
            String input = "1234567890"; // 10

            // when & then
            assertThatThrownBy(() -> Validator.maxLength(input, maxLength, ""))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        void 문자열이_null이면_통과(String input) {
            // when & then
            assertThatNoException()
                .isThrownBy(() -> Validator.maxLength(input, 10, ""));
        }
    }

    @Nested
    class minLength {

        @Test
        void 문자열의_길이가_10이고_최소_길이가_10이면_통과() {
            // given
            String input = "1234567890"; // 10
            int minLength = 10;

            // when & then
            assertThatNoException()
                .isThrownBy(() -> Validator.minLength(input, minLength, ""));
        }

        @Test
        void 문자열의_길이가_9이고_최소_길이가_10이면_예외() {
            // given
            String input = "123456789"; // 9
            int minLength = 10;

            // when & then
            assertThatThrownBy(() -> Validator.minLength(input, minLength, ""))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void 최소_길이가_0이하면_IllegalArgumentException(int minLength) {
            // given
            String input = "1234567890"; // 10

            // when & then
            assertThatThrownBy(() -> Validator.minLength(input, minLength, ""))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        void 문자열이_null이면_통과(String input) {
            // when & then
            assertThatNoException()
                .isThrownBy(() -> Validator.minLength(input, 10, ""));
        }
    }


    @Nested
    class notNull {

        @ParameterizedTest
        @NullSource
        void 객체가_null이면_예외(Object object) {
            // when & then
            assertThatThrownBy(() -> Validator.notNull(object, ""))
                .isInstanceOf(ValidException.class);
        }

        @Test
        void 객체가_null이_아니면_통과() {
            // given
            Object object = "";

            // when & then
            assertThatNoException()
                .isThrownBy(() -> Validator.notNull(object, ""));
        }
    }

    @Nested
    class maxValue {

        @Test
        void 값이_100이고_최대_값이_100이면_통과() {
            // given
            int value = 100;
            int maxValue = 100;

            // when & then
            assertThatNoException()
                .isThrownBy(() -> Validator.maxValue(value, maxValue, ""));
        }

        @Test
        void 값이_101이고_최대_값이_100이면_예외() {
            // given
            int value = 101;
            int maxValue = 100;

            // when & then
            assertThatThrownBy(() -> Validator.maxValue(value, maxValue, ""))
                .isInstanceOf(ValidException.class);
        }
    }

    @Nested
    class minValue {

        @Test
        void 값이_100이고_최소_값이_100이면_통과() {
            // given
            int value = 100;
            int minValue = 100;

            // when & then
            assertThatNoException()
                .isThrownBy(() -> Validator.minValue(value, minValue, ""));
        }

        @Test
        void 값이_99이고_최소_값이_100이면_예외() {
            // given
            int value = 99;
            int minValue = 100;

            // when & then
            assertThatThrownBy(() -> Validator.minValue(value, minValue, ""))
                .isInstanceOf(ValidException.class);
        }
    }

    @Nested
    class isNegative {

        @Test
        void 값이_음수이면_예외() {
            // given
            int value = -1;

            // when & then
            assertThatThrownBy(() -> Validator.isNegative(value, ""))
                .isInstanceOf(ValidException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void 값이_음수가_아니면_통과(int value) {
            // when & then
            assertThatNoException()
                .isThrownBy(() -> Validator.isNegative(value, ""));
        }
    }
}
