package com.festago.domain;

import static com.festago.domain.EntryState.AFTER_ENTRY;
import static com.festago.domain.EntryState.AWAY;
import static com.festago.domain.EntryState.BEFORE_ENTRY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketTest {

    @Nested
    class 출입_상태_변경 {

        @Test
        void 상태_변경시_기존의_상태와_다르면_예외() {
            // given
            MemberTicket memberTicket = new MemberTicket(new Member(1L), new Ticket(LocalDateTime.now()));

            // when & then
            assertThatThrownBy(() -> memberTicket.changeState(AFTER_ENTRY))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 출입_전_상태에서_상태를_변경하면_출입_후_상태로_변경() {
            // given
            MemberTicket memberTicket = new MemberTicket(new Member(1L), new Ticket(LocalDateTime.now()));

            // when
            memberTicket.changeState(BEFORE_ENTRY);

            // then
            assertThat(memberTicket.getEntryState()).isEqualTo(AFTER_ENTRY);
        }

        @Test
        void 출입_후_상태에서_상태를_변경하면_외출_상태로_변경() {
            // given
            MemberTicket memberTicket = new MemberTicket(new Member(1L), new Ticket(LocalDateTime.now()));
            memberTicket.changeState(BEFORE_ENTRY);

            // when
            memberTicket.changeState(AFTER_ENTRY);

            // then
            assertThat(memberTicket.getEntryState()).isEqualTo(AWAY);
        }

        @Test
        void 외출_상태에서_상태를_변경하면_출입_후_상태로_변경() {
            // given
            MemberTicket memberTicket = new MemberTicket(new Member(1L), new Ticket(LocalDateTime.now()));
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
            long memberId = 1L;
            MemberTicket memberTicket = new MemberTicket(new Member(memberId), new Ticket(LocalDateTime.now()));

            // when && then
            assertThat(memberTicket.isOwner(memberId)).isTrue();
        }

        @Test
        void 티켓_주인이_아니다() {
            // given
            long memberId = 1L;
            long ownerId = 2L;
            MemberTicket memberTicket = new MemberTicket(new Member(ownerId), new Ticket(LocalDateTime.now()));

            // when && then
            assertThat(memberTicket.isOwner(memberId)).isFalse();
        }

    }
}
