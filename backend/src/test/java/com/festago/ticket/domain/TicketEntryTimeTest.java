package com.festago.ticket.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.ValidException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketEntryTimeTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 총_수량이_0_또는_음수일_경우_에외(int amount) {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> new TicketEntryTime(now, amount))
            .isInstanceOf(ValidException.class)
            .hasMessage("amount은/는 1 이상이어야 합니다.");
    }
}
