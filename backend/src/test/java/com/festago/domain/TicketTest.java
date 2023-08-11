package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.exception.BadRequestException;
import com.festago.support.MemberFixture;
import com.festago.support.TotalityFixture;
import com.festago.support.TotalityFixture.픽스쳐를;
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
            LocalDateTime stageStartTime = LocalDateTime.parse("2023-08-12T18:00:00");
            LocalDateTime now = stageStartTime.minusWeeks(1);
            TotalityFixture fixture = 픽스쳐를.생성한다()
                .공연의().시작시간은(stageStartTime).예매오픈시간은(stageStartTime.minusHours(6))
                .이다();

            Ticket ticket = fixture.ticket();
            Stage stage = fixture.stage();
            LocalDateTime ticketOpenTime = stage.getTicketOpenTime();

            // when & then
            LocalDateTime entryTime = ticketOpenTime.minusMinutes(minute);
            assertThatThrownBy(
                () -> ticket.addTicketEntryTime(now, entryTime, 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("입장 시간은 티켓 오픈 시간 이후여야합니다.");
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1})
        void 입장_시간이_공연_시작_시간보다_같거나_이후면_예외(long minute) {
            // given
            LocalDateTime stageStartTime = LocalDateTime.parse("2023-08-12T18:00:00");
            LocalDateTime now = stageStartTime.minusWeeks(1);
            TotalityFixture fixture = 픽스쳐를.생성한다()
                .공연의().시작시간은(stageStartTime).예매오픈시간은(stageStartTime.minusHours(6))
                .이다();

            Ticket ticket = fixture.ticket();

            // when & then
            LocalDateTime entryTime = stageStartTime.plusMinutes(minute);
            assertThatThrownBy(() -> ticket.addTicketEntryTime(now, entryTime, 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("입장 시간은 공연 시간보다 빨라야합니다.");
        }

        @Test
        void 입장_시간이_공연_시작_12시간_이전이면_예외() {
            // given
            LocalDateTime stageStartTime = LocalDateTime.parse("2023-08-12T18:00:00");
            LocalDateTime now = stageStartTime.minusWeeks(1);
            TotalityFixture fixture = 픽스쳐를.생성한다()
                .공연의().시작시간은(stageStartTime).예매오픈시간은(stageStartTime.minusHours(13))
                .이다();

            Ticket ticket = fixture.ticket();

            // when & then
            LocalDateTime entryTime = stageStartTime.minusHours(12).minusSeconds(1);
            assertThatThrownBy(() -> ticket.addTicketEntryTime(now, entryTime, 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("입장 시간은 공연 시작 12시간 이내여야 합니다.");
        }

        @Test
        void 티켓_오픈_이후_티켓생성시_예외() {
            // given
            LocalDateTime ticketOpenTime = LocalDateTime.parse("2023-08-12T12:00:00");
            TotalityFixture fixture = 픽스쳐를.생성한다()
                .공연의().시작시간은(ticketOpenTime.plusHours(6)).예매오픈시간은(ticketOpenTime)
                .이다();
            Ticket ticket = fixture.ticket();

            // when & then
            LocalDateTime now = ticketOpenTime.plusSeconds(1);
            assertThatThrownBy(
                () -> ticket.addTicketEntryTime(now, ticketOpenTime.plusHours(1), 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("티켓 예매 시작 후 새롭게 티켓을 발급할 수 없습니다.");
        }

        @Test
        void 입장시간을_추가한다() {
            // given
            LocalDateTime stageStartTime = LocalDateTime.parse("2023-08-12T18:00:00");
            LocalDateTime now = stageStartTime.minusWeeks(1);
            TotalityFixture fixture = 픽스쳐를.생성한다()
                .공연의().시작시간은(stageStartTime).예매오픈시간은(stageStartTime.minusHours(6))
                .이다();

            Ticket ticket = fixture.ticket();

            // when
            ticket.addTicketEntryTime(now, stageStartTime.minusHours(3), 100);
            ticket.addTicketEntryTime(now, stageStartTime.minusHours(2), 200);

            // then
            assertSoftly(softly -> {
                softly.assertThat(ticket.getTicketAmount().getTotalAmount()).isEqualTo(300);
                softly.assertThat(ticket.getTicketEntryTimes()).hasSize(2);
            });
        }
    }

    @Nested
    class 예매_티켓_생성 {

        @Test
        void 최대_수량보다_많으면_예외() {
            // given
            Member member = MemberFixture.member()
                .build();
            LocalDateTime stageStartTime = LocalDateTime.parse("2023-08-12T18:00:00");
            LocalDateTime now = stageStartTime.minusWeeks(1);
            TotalityFixture fixture = 픽스쳐를.생성한다()
                .공연의().시작시간은(stageStartTime).예매오픈시간은(stageStartTime.minusHours(6))
                .이다();
            Ticket ticket = fixture.ticket();

            ticket.addTicketEntryTime(now, stageStartTime.minusHours(1), 50);
            ticket.addTicketEntryTime(now, stageStartTime.minusHours(2), 30);
            ticket.addTicketEntryTime(now, stageStartTime.minusHours(3), 20);

            // when & then
            assertThatThrownBy(() -> ticket.createMemberTicket(member, 101))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("매진된 티켓입니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 100})
        void 성공(int reservationSequence) {
            // given
            Member member = MemberFixture.member()
                .build();
            LocalDateTime stageStartTime = LocalDateTime.parse("2023-08-12T18:00:00");
            LocalDateTime now = stageStartTime.minusWeeks(1);
            TotalityFixture fixture = 픽스쳐를.생성한다()
                .공연의().시작시간은(stageStartTime).예매오픈시간은(stageStartTime.minusHours(6))
                .이다();
            Ticket ticket = fixture.ticket();

            ticket.addTicketEntryTime(now, stageStartTime.minusHours(1), 50);
            ticket.addTicketEntryTime(now, stageStartTime.minusHours(2), 30);
            ticket.addTicketEntryTime(now, stageStartTime.minusHours(3), 20);

            // when
            MemberTicket memberTicket = ticket.createMemberTicket(member, reservationSequence);

            // then
            assertThat(memberTicket.getOwner()).isEqualTo(member);
        }
    }
}
