package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.exception.BadRequestException;
import com.festago.support.MemberFixture;
import com.festago.support.TicketFixture;
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

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void 입장_시간이_축제_시작_시간보다_같거나_이후면_예외(long minute) {
        // given
        Ticket ticket = TicketFixture.ticket()
            .build();

        LocalDateTime stageStartTime = ticket.getStage().getStartTime();
        LocalDateTime entryTime = stageStartTime.plusMinutes(minute);

        // when & then
        assertThatThrownBy(() -> ticket.addTicketEntryTime(entryTime, 100))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("입장 시간은 공연 시간보다 빨라야합니다.");
    }

    @Test
    void 입장_시간이_공연_시작_12시간_이전이면_예외() {
        // given
        Ticket ticket = TicketFixture.ticket()
            .build();

        LocalDateTime stageStartTime = ticket.getStage().getStartTime();
        LocalDateTime entryTime = stageStartTime.minusHours(12).minusSeconds(1);

        // when & then
        assertThatThrownBy(() -> ticket.addTicketEntryTime(entryTime, 100))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("입장 시간은 공연 시작 12시간 이내여야 합니다.");
    }

    @Test
    void 입장시간을_추가한다() {
        // given
        Ticket ticket = TicketFixture
            .ticket()
            .build();

        LocalDateTime startTime = ticket.getStage().getStartTime();

        // when
        ticket.addTicketEntryTime(startTime.minusHours(3), 100);
        ticket.addTicketEntryTime(startTime.minusHours(2), 200);

        // then
        assertSoftly(softly -> {
            softly.assertThat(ticket.getTicketAmount().getTotalAmount()).isEqualTo(300);
            softly.assertThat(ticket.getTicketEntryTimes()).hasSize(2);
        });
    }

    @Nested
    class 예매_티켓_생성 {

        @Test
        void 최대_수량보다_많으면_예외() {
            // given
            Member member = MemberFixture.member()
                .build();
            Ticket ticket = TicketFixture.ticket()
                .build();
            ticket.addTicketEntryTime(LocalDateTime.now().minusHours(1), 50);
            ticket.addTicketEntryTime(LocalDateTime.now().minusHours(2), 30);
            ticket.addTicketEntryTime(LocalDateTime.now().minusHours(3), 20);

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
            Ticket ticket = TicketFixture.ticket()
                .build();
            ticket.addTicketEntryTime(LocalDateTime.now().minusHours(1), 50);
            ticket.addTicketEntryTime(LocalDateTime.now().minusHours(2), 30);
            ticket.addTicketEntryTime(LocalDateTime.now().minusHours(3), 20);

            // when
            MemberTicket memberTicket = ticket.createMemberTicket(member, reservationSequence);

            // then
            assertThat(memberTicket.getOwner()).isEqualTo(member);
        }
    }
}
