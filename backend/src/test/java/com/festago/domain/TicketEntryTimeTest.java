package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.exception.BadRequestException;
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
    void 총_수량이_1_미만이면_예외(int totalAmount) {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> new TicketEntryTime(now, totalAmount))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("티켓은 적어도 한장 이상 발급해야합니다.");
    }
}
