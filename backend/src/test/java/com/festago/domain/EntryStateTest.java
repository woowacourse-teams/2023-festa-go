package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.exception.InternalServerException;
import com.festago.ticketing.domain.EntryState;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EntryStateTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 3})
    void 유효하지않은_인덱스로_생성시_예외(int index) {
        // when & then
        assertThatThrownBy(() -> EntryState.from(index))
            .isInstanceOf(InternalServerException.class)
            .hasMessage("올바르지 않은 입장상태 인덱스입니다.");
    }

    @Test
    void 인덱스로_생성시_인자가_null이면_예외() {
        // when & then
        assertThatThrownBy(() -> EntryState.from(null))
            .isInstanceOf(InternalServerException.class)
            .hasMessage("올바르지 않은 입장상태 인덱스입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void 인덱스로_생성_성공(int index) {
        // when & then
        assertThatNoException().isThrownBy(() -> EntryState.from(index));
    }
}
