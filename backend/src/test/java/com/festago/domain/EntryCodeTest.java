package com.festago.domain;

import static com.festago.common.exception.ErrorCode.INVALID_ENTRY_CODE_OFFSET;
import static com.festago.common.exception.ErrorCode.INVALID_ENTRY_CODE_PERIOD;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.festago.common.exception.InternalServerException;
import com.festago.entry.domain.EntryCode;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EntryCodeTest {

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void 입장_코드의_수명이_0_또는_음수이면_예외(long period) {
        // when & then
        assertThatThrownBy(() -> new EntryCode("code", period, 0))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(INVALID_ENTRY_CODE_PERIOD.getMessage());
    }

    @Test
    void 입장_코드의_오프셋이_음수이면_예외() {
        // when & tehn
        assertThatThrownBy(() -> new EntryCode("code", 30, -1))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(INVALID_ENTRY_CODE_OFFSET.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void 입장_코드의_오프셋이_0이상이면_성공(long offset) {
        assertThatNoException()
            .isThrownBy(() -> new EntryCode("code", 30, offset));
    }
}
