package com.festago.ticket.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.ValidException;
import com.festago.support.fixture.StageTicketEntryTimeFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageTicketEntryTimesTest {

    @Nested
    class calculateEntryTime {

        /**
         * 입장 시간 별 10개의 티켓이 있다.
         */
        LocalDateTime 첫번째_입장_시간 = LocalDateTime.parse("2077-06-30T18:00:00");
        LocalDateTime 두번째_입장_시간 = LocalDateTime.parse("2077-06-30T19:00:00");
        LocalDateTime 세번째_입장_시간 = LocalDateTime.parse("2077-06-30T20:00:00");

        StageTicketEntryTimes stageTicketEntryTimes;

        @BeforeEach
        void setUp() {
            stageTicketEntryTimes = new StageTicketEntryTimes();
            stageTicketEntryTimes.add(StageTicketEntryTimeFixture.builder()
                .amount(10)
                .entryTime(첫번째_입장_시간)
                .build());
            stageTicketEntryTimes.add(StageTicketEntryTimeFixture.builder()
                .amount(10)
                .entryTime(두번째_입장_시간)
                .build());
            stageTicketEntryTimes.add(StageTicketEntryTimeFixture.builder()
                .amount(10)
                .entryTime(세번째_입장_시간)
                .build());
        }

        @ValueSource(ints = {1, 10})
        @ParameterizedTest
        void 첫번째_입장_시간에_해당하는_순번은_첫번째_입장_시간을_반환한다(int sequence) {
            // when
            LocalDateTime entryTime = stageTicketEntryTimes.calculateEntryTime(sequence);

            // then
            assertThat(entryTime).isEqualTo(첫번째_입장_시간);
        }

        @ValueSource(ints = {11, 20})
        @ParameterizedTest
        void 두번째_입장_시간에_해당하는_순번은_두번째_입장_시간을_반환한다(int sequence) {
            // when
            LocalDateTime entryTime = stageTicketEntryTimes.calculateEntryTime(sequence);

            // then
            assertThat(entryTime).isEqualTo(두번째_입장_시간);
        }

        @ValueSource(ints = {21, 30})
        @ParameterizedTest
        void 세번째_입장_시간에_해당하는_순번은_세번째_입장_시간을_반환한다(int sequence) {
            // when
            LocalDateTime entryTime = stageTicketEntryTimes.calculateEntryTime(sequence);

            // then
            assertThat(entryTime).isEqualTo(세번째_입장_시간);
        }

        @ValueSource(ints = {0, -1})
        @ParameterizedTest
        void 순번이_0_이하이면_예외(int sequence) {
            // when & then
            assertThatThrownBy(() -> stageTicketEntryTimes.calculateEntryTime(sequence))
                .isInstanceOf(ValidException.class);
        }

        @Test
        void 순번이_입장_시간의_티켓_수를_초과하면_예외() {
            // given
            int sequence = 31;

            // when & then
            assertThatThrownBy(() -> stageTicketEntryTimes.calculateEntryTime(sequence))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.TICKET_SOLD_OUT.getMessage());
        }

        @ValueSource(ints = {1, 10, 11, 20})
        @ParameterizedTest
        void 입장_시간이_동일한_경우_같은_입장_시간을_반환한다(int sequence) {
            // given
            stageTicketEntryTimes = new StageTicketEntryTimes();
            stageTicketEntryTimes.add(StageTicketEntryTimeFixture.builder()
                .amount(10)
                .entryTime(첫번째_입장_시간)
                .build());
            stageTicketEntryTimes.add(StageTicketEntryTimeFixture.builder()
                .amount(10)
                .entryTime(첫번째_입장_시간)
                .build());

            // when
            assertThat(stageTicketEntryTimes.calculateEntryTime(sequence))
                .isEqualTo(첫번째_입장_시간);
        }
    }
}
