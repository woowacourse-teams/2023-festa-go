package com.festago.festago.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SentenceValidationTest {

    @ParameterizedTest
    @ValueSource(strings = ["asdf", "fkwk", "test"])
    fun `4자리 알파벳 소문자이면 검증된 문자이다`(sentence: String) {
        val sentenceValidation = SentenceValidation.of("abcdefghijklmnopqrstuvwxyz", 4)

        assertThat(sentenceValidation.isValidate(sentence)).isTrue
    }

    @ParameterizedTest
    @ValueSource(strings = ["12345", "23456", "20382"])
    fun `5자리 숫자이면 검증된 문자이다`(sentence: String) {
        val sentenceValidation = SentenceValidation.of("0123456789", 5)

        assertThat(sentenceValidation.isValidate(sentence)).isTrue
    }

    @ParameterizedTest
    @ValueSource(strings = ["asd12", "145667", "11"])
    fun `4자리 숫자가 아니면 검증된 문자이다`(sentence: String) {
        val sentenceValidation = SentenceValidation.of("0123456789", 4)

        assertThat(sentenceValidation.isValidate(sentence)).isFalse
    }
}
