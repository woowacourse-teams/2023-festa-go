package com.festago.ticketing.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.support.fixture.ReserveTicketFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReserveTicketTest {

    @Nested
    class changeState {

        @ParameterizedTest
        @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = "BEFORE_ENTRY")
        void 입장_상태를_변경할때_원본_상태와_다르면_변경되지_않는다(EntryState originState) {
            // given
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).build();

            // when
            reserveTicket.changeState(originState);

            // then
            assertThat(reserveTicket.getEntryState()).isEqualTo(EntryState.BEFORE_ENTRY);
        }

        @Test
        void 입장_상태를_변경할때_원본_상태와_같으면_변경된다() {
            // given
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).build();

            // when
            reserveTicket.changeState(EntryState.BEFORE_ENTRY);

            // then
            assertThat(reserveTicket.getEntryState()).isEqualTo(EntryState.AFTER_ENTRY);
        }

        @Test
        void 입장_상태가_AFTER_ENTRY_일때_다음_상태는_AWAY_이다() {
            // given
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).build();
            reserveTicket.changeState(EntryState.BEFORE_ENTRY);

            // when
            reserveTicket.changeState(EntryState.AFTER_ENTRY);

            // then
            assertThat(reserveTicket.getEntryState()).isEqualTo(EntryState.AWAY);
        }

        @Test
        void 입장_상태가_AWAY_일때_다음_상태는_AFTER_ENTRY_이다() {
            // given
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).build();
            reserveTicket.changeState(EntryState.BEFORE_ENTRY);
            reserveTicket.changeState(EntryState.AFTER_ENTRY);

            // when
            reserveTicket.changeState(EntryState.AWAY);

            // then
            assertThat(reserveTicket.getEntryState()).isEqualTo(EntryState.AFTER_ENTRY);
        }
    }

    @Nested
    class isOwner {

        @Test
        void 티켓의_주인과_다르면_거짓() {
            // given
            Long memberId = 1L;
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).memberId(4885L).build();

            // when
            boolean actual = reserveTicket.isOwner(memberId);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 티켓의_주인과_일치하면_참() {
            // given
            Long memberId = 4885L;
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).memberId(4885L).build();

            // when
            boolean actual = reserveTicket.isOwner(memberId);

            // then
            assertThat(actual).isTrue();
        }
    }

    @Nested
    class isBeforeEntry {

        @Test
        void 입장_시간_이전이면_참() {
            // given
            LocalDateTime now = LocalDateTime.parse("2077-06-30T17:59:59");
            LocalDateTime entryTime = LocalDateTime.parse("2077-06-30T18:00:00");
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).entryTime(entryTime).build();

            // when
            boolean actual = reserveTicket.isBeforeEntry(now);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 입장_시간과_같으면_거짓() {
            // given
            LocalDateTime now = LocalDateTime.parse("2077-06-30T18:00:00");
            LocalDateTime entryTime = LocalDateTime.parse("2077-06-30T18:00:00");
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).entryTime(entryTime).build();

            // when
            boolean actual = reserveTicket.isBeforeEntry(now);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 입장_시간_이후이면_거짓() {
            // given
            LocalDateTime now = LocalDateTime.parse("2077-06-30T18:00:01");
            LocalDateTime entryTime = LocalDateTime.parse("2077-06-30T18:00:00");
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).entryTime(entryTime).build();

            // when
            boolean actual = reserveTicket.isBeforeEntry(now);

            // then
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class canEntry {

        @Test
        void 입장_시간_이전이면_거짓() {
            // given
            LocalDateTime now = LocalDateTime.parse("2077-06-30T17:59:59");
            LocalDateTime entryTime = LocalDateTime.parse("2077-06-30T18:00:00");
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).entryTime(entryTime).build();

            // when
            boolean actual = reserveTicket.canEntry(now);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 입장_시간과_같으면_참() {
            // given
            LocalDateTime now = LocalDateTime.parse("2077-06-30T18:00:00");
            LocalDateTime entryTime = LocalDateTime.parse("2077-06-30T18:00:00");
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).entryTime(entryTime).build();

            // when
            boolean actual = reserveTicket.canEntry(now);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 입장_시간_이후이면_참() {
            // given
            LocalDateTime now = LocalDateTime.parse("2077-06-30T18:00:01");
            LocalDateTime entryTime = LocalDateTime.parse("2077-06-30T18:00:00");
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).entryTime(entryTime).build();

            // when
            boolean actual = reserveTicket.canEntry(now);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 입장_시간_이후_24시간을_초과하면_거짓() {
            // given
            LocalDateTime now = LocalDateTime.parse("2077-06-30T18:00:00").plusHours(24);
            LocalDateTime entryTime = LocalDateTime.parse("2077-06-30T18:00:00");
            ReserveTicket reserveTicket = ReserveTicketFixture.builder().id(1L).entryTime(entryTime).build();

            // when
            boolean actual = reserveTicket.canEntry(now);

            // then
            assertThat(actual).isFalse();
        }
    }
}
