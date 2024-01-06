package com.festago.ticket.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationSequenceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 예약_순서는_양의_1보다_커야한다(int sequence) {
        // given & when & then
        assertThatThrownBy(() -> new ReservationSequence(sequence))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(ErrorCode.TICKET_SEQUENCE_DATA_ERROR.getMessage());
    }

}
