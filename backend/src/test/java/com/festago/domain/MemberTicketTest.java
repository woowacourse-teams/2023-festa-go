package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketTest {

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
