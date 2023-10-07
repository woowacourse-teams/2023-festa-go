package com.festago.ticketing.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("entryState의 인덱스가 올바르지 않습니다.");
    }

    @Test
    void 인덱스로_생성시_인자가_null이면_예외() {
        // when & then
        assertThatThrownBy(() -> EntryState.from(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("entryState의 인덱스는 null이 될 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void 인덱스로_생성_성공(int index) {
        // when & then
        assertThatNoException().isThrownBy(() -> EntryState.from(index));
    }
}
