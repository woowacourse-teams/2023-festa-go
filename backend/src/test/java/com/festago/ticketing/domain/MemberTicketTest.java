package com.festago.ticketing.domain;

import static com.festago.ticketing.domain.EntryState.AFTER_ENTRY;
import static com.festago.ticketing.domain.EntryState.AWAY;
import static com.festago.ticketing.domain.EntryState.BEFORE_ENTRY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.domain.Festival;
import com.festago.member.domain.Member;
import com.festago.stage.domain.Stage;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.MemberFixture;
import com.festago.support.fixture.MemberTicketFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.support.fixture.TicketFixture;
import com.festago.ticket.domain.ReservationSequence;
import com.festago.ticket.domain.Ticket;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketTest {

    @Nested
    class 티켓_생성 {

        @Test
        void 예매_시간_이후에_생성하면_예외() {
            // given
            LocalDateTime stageStartTime = LocalDateTime.parse("2022-08-12T18:00:00");
            LocalDateTime now = stageStartTime.plusHours(1);
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

            ReservationSequence sequence = new ReservationSequence(1);

            // when & then
            assertThatThrownBy(() -> MemberTicket.createMemberTicket(ticket, member, sequence, now))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.TICKET_CANNOT_RESERVE_STAGE_START.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 100})
        void 성공(int reservationSequence) {
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

            ticket.addTicketEntryTime(LocalDateTime.MIN, stageStartTime.minusHours(1), 50);
            ticket.addTicketEntryTime(LocalDateTime.MIN, stageStartTime.minusHours(2), 30);
            ticket.addTicketEntryTime(LocalDateTime.MIN, stageStartTime.minusHours(3), 20);

            ReservationSequence sequence = new ReservationSequence(reservationSequence);

            // when
            MemberTicket memberTicket = MemberTicket.createMemberTicket(ticket, member, sequence, now);

            // then
            assertThat(memberTicket.getOwner()).isEqualTo(member);
        }
    }

    @Nested
    class 입장_가능_여부_검사 {

        @Test
        void 입장시간_전_입장_불가() {
            // given
            LocalDateTime entryTime = LocalDateTime.now();
            LocalDateTime time = entryTime.minusMinutes(10);

            MemberTicket memberTicket = MemberTicketFixture.builder()
                .entryTime(entryTime)
                .build();

            // when && then
            assertThat(memberTicket.canEntry(time)).isFalse();
        }

        @Test
        void 입장시간_24시간이_지나면_입장_불가() {
            // given
            LocalDateTime entryTime = LocalDateTime.now();
            LocalDateTime time = entryTime.plusHours(24);

            MemberTicket memberTicket = MemberTicketFixture.builder()
                .entryTime(entryTime)
                .build();

            // when && then
            assertThat(memberTicket.canEntry(time)).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"2023-07-28T17:59:59", "2023-07-27T18:00:00"})
        void 입장_가능(LocalDateTime time) {
            // given
            LocalDateTime entryTime = LocalDateTime.parse("2023-07-27T18:00:00");
            Festival festival = FestivalFixture.builder()
                .startDate(entryTime.toLocalDate())
                .endDate(entryTime.plusDays(4).toLocalDate())
                .build();
            Stage stage = StageFixture.builder()
                .startTime(entryTime.plusHours(4))
                .ticketOpenTime(entryTime.minusWeeks(1))
                .festival(festival)
                .build();
            MemberTicket memberTicket = MemberTicketFixture.builder()
                .stage(stage)
                .entryTime(entryTime)
                .build();

            // when && then
            assertThat(memberTicket.canEntry(time)).isTrue();
        }
    }

    @Nested
    class 대기상태_티켓_검사 {

        @Test
        void 입장시간_이후이면_거짓() {
            // given
            LocalDateTime entryTime = LocalDateTime.now();
            LocalDateTime time = entryTime.plusHours(1);

            MemberTicket memberTicket = MemberTicketFixture.builder()
                .entryTime(entryTime)
                .build();

            // when & then
            assertThat(memberTicket.isBeforeEntry(time)).isFalse();
        }

        @Test
        void 입장시간_이전이면_참() {
            // given
            LocalDateTime entryTime = LocalDateTime.now();
            LocalDateTime time = entryTime.minusHours(12).plusSeconds(1);
            Festival festival = FestivalFixture.builder()
                .startDate(entryTime.toLocalDate())
                .endDate(entryTime.plusDays(4).toLocalDate())
                .build();
            Stage stage = StageFixture.builder()
                .startTime(entryTime.plusHours(4))
                .festival(festival)
                .build();
            MemberTicket memberTicket = MemberTicketFixture.builder()
                .stage(stage)
                .entryTime(entryTime)
                .build();

            // when & then
            assertThat(memberTicket.isBeforeEntry(time)).isTrue();
        }
    }

    @Nested
    class 출입_상태_변경 {

        @Test
        void 상태_변경시_기존의_상태와_다르면_기존_상태가_유지된다() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.builder().build();

            // when
            memberTicket.changeState(AFTER_ENTRY);

            // then
            assertThat(memberTicket.getEntryState()).isEqualTo(BEFORE_ENTRY);
        }

        @Test
        void 출입_전_상태에서_상태를_변경하면_출입_후_상태로_변경() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.builder().build();

            // when
            memberTicket.changeState(BEFORE_ENTRY);

            // then
            assertThat(memberTicket.getEntryState()).isEqualTo(AFTER_ENTRY);
        }

        @Test
        void 출입_후_상태에서_상태를_변경하면_외출_상태로_변경() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.builder().build();
            memberTicket.changeState(BEFORE_ENTRY);

            // when
            memberTicket.changeState(AFTER_ENTRY);

            // then
            assertThat(memberTicket.getEntryState()).isEqualTo(AWAY);
        }

        @Test
        void 외출_상태에서_상태를_변경하면_출입_후_상태로_변경() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.builder().build();
            memberTicket.changeState(BEFORE_ENTRY);
            memberTicket.changeState(AFTER_ENTRY);

            // when
            memberTicket.changeState(AWAY);

            // then
            assertThat(memberTicket.getEntryState()).isEqualTo(AFTER_ENTRY);
        }
    }

    @Nested
    class 티켓_주인_검사 {

        @Test
        void 티켓_주인이다() {
            // given
            Long memberId = 1L;
            Member member = new Member(memberId);
            MemberTicket memberTicket = MemberTicketFixture.builder()
                .owner(member)
                .build();

            // when && then
            assertThat(memberTicket.isOwner(memberId)).isTrue();
        }

        @Test
        void 티켓_주인이_아니다() {
            // given
            Long memberId = 1L;
            Long ownerId = 2L;
            Member owner = new Member(ownerId);
            MemberTicket memberTicket = MemberTicketFixture.builder()
                .owner(owner)
                .build();

            // when && then
            assertThat(memberTicket.isOwner(memberId)).isFalse();
        }
    }
}
