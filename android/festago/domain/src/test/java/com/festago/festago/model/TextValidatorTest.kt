package com.festago.festago.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class TextValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = ["asdf", "fkwk", "test"])
    fun `알파벳만 허용하고 기대 길이가 4 일 때 다음은 검증된 Text 이다`(text: String) {
        // given
        val textValidator = TextValidator.of("abcdefghijklmnopqrstuvwxyz", 4)

        // when
        val isValidText = textValidator.isValid(text)

        // then
        assertThat(isValidText).isTrue
    }

    @ParameterizedTest
    @ValueSource(strings = ["12345", "23456", "20382"])
    fun `숫자만 허용하고 기대 길이가 5 일 때 다음은 검증된 Text 이다`(text: String) {
        // given
        val textValidator = TextValidator.of("0123456789", 5)

        // when
        val isValidText = textValidator.isValid(text)

        // then
        assertThat(isValidText).isTrue
    }

    @ParameterizedTest
    @ValueSource(strings = ["asd12", "145667", "11"])
    fun `숫자만 허용하고 기대 길이가 4 일 때 다음은 검증된 Text 가 아니다`(text: String) {
        // given
        val textValidator = TextValidator.of("0123456789", 4)

        // when
        val isValidText = textValidator.isValid(text)

        // then
        assertThat(isValidText).isFalse
    }
}
