package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.festago.exception.InternalServerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EntryCodeTimeTest {

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void 입장_코드의_수명이_0_또는_음수이면_예외(long period) {
        // when & then
        assertThatThrownBy(() -> EntryCodeTime.of(period, 0))
            .isInstanceOf(InternalServerException.class)
            .hasMessage("올바르지 않은 입장코드 유효기간입니다.");
    }

    @Test
    void 입장_코드의_오프셋이_음수이면_예외() {
        // when & tehn
        assertThatThrownBy(() -> EntryCodeTime.of(30, -1))
            .isInstanceOf(InternalServerException.class)
            .hasMessage("올바르지 않은 입장코드 오프셋입니다.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void 입장_코드의_오프셋이_0이상이면_성공(long offset) {
        assertThatNoException()
            .isThrownBy(() -> EntryCodeTime.of(30, offset));
    }
}
