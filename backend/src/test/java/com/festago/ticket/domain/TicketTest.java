package com.festago.ticket.domain;

import static com.festago.common.exception.ErrorCode.EARLY_TICKET_ENTRY_THAN_OPEN;
import static com.festago.common.exception.ErrorCode.EARLY_TICKET_ENTRY_TIME;
import static com.festago.common.exception.ErrorCode.INVALID_TICKET_CREATE_TIME;
import static com.festago.common.exception.ErrorCode.LATE_TICKET_ENTRY_TIME;
import static com.festago.common.exception.ErrorCode.TICKET_SOLD_OUT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.common.exception.BadRequestException;
import com.festago.festival.domain.Festival;
import com.festago.member.domain.Member;
import com.festago.stage.domain.Stage;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.MemberFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.support.fixture.TicketFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketTest {

    @Nested
    class 입장시간_추가_검증 {

        @ParameterizedTest
        @ValueSource(longs = {0, 1})
        void 입장시간이_티켓오픈시간_이전이면_예외(long minute) {
            // given
            LocalDateTime now = LocalDateTime.now();
            Stage stage = StageFixture.builder()
                .startTime(now.plusDays(1))
                .ticketOpenTime(now)
                .build();
            Ticket ticket = TicketFixture.builder()
                .stage(stage)
                .build();

            // when & then
            assertThatThrownBy(
                () -> ticket.addTicketEntryTime(now.minusMinutes(10), now.minusMinutes(minute), 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(EARLY_TICKET_ENTRY_THAN_OPEN.getMessage());
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1})
        void 입장_시간이_축제_시작_시간보다_같거나_이후면_예외(long minute) {
            // given
            Ticket ticket = TicketFixture.builder()
                .build();

            Stage stage = ticket.getStage();
            LocalDateTime stageStartTime = stage.getStartTime();
            LocalDateTime entryTime = stageStartTime.plusMinutes(minute);
            LocalDateTime ticketOpenTime = stage.getTicketOpenTime();

            // when & then
            assertThatThrownBy(() -> ticket.addTicketEntryTime(ticketOpenTime.minusMinutes(10), entryTime, 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(LATE_TICKET_ENTRY_TIME.getMessage());
        }

        @Test
        void 입장_시간이_공연_시작_12시간_이전이면_예외() {
            // given
            Ticket ticket = TicketFixture.builder()
                .build();

            Stage stage = ticket.getStage();
            LocalDateTime stageStartTime = stage.getStartTime();
            LocalDateTime entryTime = stageStartTime.minusHours(12).minusSeconds(1);
            LocalDateTime ticketOpenTime = stage.getTicketOpenTime();

            // when & then
            assertThatThrownBy(() -> ticket.addTicketEntryTime(ticketOpenTime.minusMinutes(10), entryTime, 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(EARLY_TICKET_ENTRY_TIME.getMessage());
        }

        @Test
        void 티켓_오픈_이후_티켓생성시_예외() {
            // given
            Stage stage = StageFixture.builder()
                .ticketOpenTime(LocalDateTime.now().minusHours(1))
                .build();
            Ticket ticket = TicketFixture.builder()
                .build();

            LocalDateTime startTime = stage.getStartTime();

            // when & then
            assertThatThrownBy(() -> ticket.addTicketEntryTime(LocalDateTime.now(), startTime.minusHours(3), 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(INVALID_TICKET_CREATE_TIME.getMessage());
        }

        @Test
        void 입장시간을_추가한다() {
            // given
            Ticket ticket = TicketFixture
                .builder()
                .build();

            Stage stage = ticket.getStage();
            LocalDateTime startTime = stage.getStartTime();
            LocalDateTime ticketOpenTime = stage.getTicketOpenTime();

            // when
            ticket.addTicketEntryTime(ticketOpenTime.minusMinutes(10), startTime.minusHours(3), 100);
            ticket.addTicketEntryTime(ticketOpenTime.minusMinutes(10), startTime.minusHours(2), 200);

            // then
            assertSoftly(softly -> {
                softly.assertThat(ticket.getTicketAmount().getTotalAmount()).isEqualTo(300);
                softly.assertThat(ticket.getTicketEntryTimes()).hasSize(2);
            });
        }
    }

    @Nested
    class 티켓_정보_추출 {

        @Test
        void 최대_수량보다_많으면_예외() {
            // given
            LocalDateTime stageStartTime = LocalDateTime.parse("2022-08-12T18:00:00");
            LocalDateTime now = stageStartTime.minusHours(6);
            Festival festival = FestivalFixture.builder()
                .startDate(stageStartTime.toLocalDate())
                .endDate(stageStartTime.toLocalDate())
                .build();
            Stage stage = StageFixture.builder()
                .startTime(stageStartTime)
                .ticketOpenTime(stageStartTime.minusDays(1))
                .festival(festival)
                .build();
            Ticket ticket = TicketFixture.builder()
                .stage(stage)
                .build();
            Member member = MemberFixture.builder()
                .id(1L)
                .build();

            ReservationSequence overSequence = new ReservationSequence(101);

            // when & then
            assertThatThrownBy(() -> ticket.extractTicketInfo(overSequence))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(TICKET_SOLD_OUT.getMessage());
        }
    }
}
