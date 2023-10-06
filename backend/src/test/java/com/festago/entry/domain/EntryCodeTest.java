package com.festago.entry.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EntryCodeTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    void 입장_코드의_코드가_null_또는_공백이면_예외(String code) {
        assertThatThrownBy(() -> new EntryCode(code, 30, 10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("code는 빈 값 또는 null이 될 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void 입장_코드의_수명이_0_또는_음수이면_예외(long period) {
        // when & then
        assertThatThrownBy(() -> new EntryCode("code", period, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("period는 0 또는 음수가 될 수 없습니다.");
    }

    @Test
    void 입장_코드의_오프셋이_음수이면_예외() {
        // when & then
        assertThatThrownBy(() -> new EntryCode("code", 30, -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("offset은 음수가 될 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void 입장_코드의_오프셋이_0이상이면_성공(long offset) {
        assertThatNoException()
            .isThrownBy(() -> new EntryCode("code", 30, offset));
    }
}
