package com.festago.domain;

import static com.festago.domain.EntryState.AFTER_ENTRY;
import static com.festago.domain.EntryState.AWAY;
import static com.festago.domain.EntryState.BEFORE_ENTRY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.support.MemberTicketFixture;
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
        void 상태_변경시_기존의_상태와_다르면_기존_상태가_유지된다() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.memberTicket().build();

            // when
            memberTicket.changeState(AFTER_ENTRY);

            // then
            assertThat(memberTicket.getEntryState()).isEqualTo(BEFORE_ENTRY);
        }

        @Test
        void 출입_전_상태에서_상태를_변경하면_출입_후_상태로_변경() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.memberTicket().build();

            // when
            memberTicket.changeState(BEFORE_ENTRY);

            // then
            assertThat(memberTicket.getEntryState()).isEqualTo(AFTER_ENTRY);
        }

        @Test
        void 출입_후_상태에서_상태를_변경하면_외출_상태로_변경() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.memberTicket().build();
            memberTicket.changeState(BEFORE_ENTRY);

            // when
            memberTicket.changeState(AFTER_ENTRY);

            // then
            assertThat(memberTicket.getEntryState()).isEqualTo(AWAY);
        }

        @Test
        void 외출_상태에서_상태를_변경하면_출입_후_상태로_변경() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.memberTicket().build();
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
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
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
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .owner(owner)
                .build();

            // when && then
            assertThat(memberTicket.isOwner(memberId)).isFalse();
        }

    }
}
