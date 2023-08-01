package com.festago.domain;

import static com.festago.domain.EntryState.AFTER_ENTRY;
import static com.festago.domain.EntryState.AWAY;
import static com.festago.domain.EntryState.BEFORE_ENTRY;
import static org.assertj.core.api.Assertions.assertThat;

import com.festago.support.FestivalFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.StageFixture;
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
    class 입장_가능_여부_검사 {

        @Test
        void 입장시간_전_입장_불가() {
            // given
            LocalDateTime entryTime = LocalDateTime.now();
            LocalDateTime time = entryTime.minusMinutes(10);

            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
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

            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
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
            Festival festival = FestivalFixture.festival()
                .startDate(entryTime.toLocalDate())
                .endDate(entryTime.plusDays(4).toLocalDate())
                .build();
            Stage stage = StageFixture.stage()
                .startTime(entryTime.plusHours(4))
                .festival(festival)
                .build();
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .stage(stage)
                .entryTime(entryTime)
                .build();

            // when && then
            assertThat(memberTicket.canEntry(time)).isTrue();
        }
    }

    @Nested
    class 현재_활성화_검사 {

        @Test
        void 입장_가능시간이_지나면_거짓() {
            // given
            LocalDateTime entryTime = LocalDateTime.now();
            LocalDateTime time = entryTime.plusHours(24);

            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .entryTime(entryTime)
                .build();

            // when & then
            assertThat(memberTicket.isCurrent(time)).isFalse();
        }

        @Test
        void 입장시간_12시간전이면_거짓() {
            // given
            LocalDateTime entryTime = LocalDateTime.now();
            LocalDateTime time = entryTime.minusHours(12);

            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .entryTime(entryTime)
                .build();

            // when & then
            assertThat(memberTicket.isCurrent(time)).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"2023-07-28T17:59:59", "2023-07-27T18:00:00"})
        void 입장_가능하면_참(LocalDateTime time) {
            // given
            LocalDateTime entryTime = LocalDateTime.parse("2023-07-27T18:00:00");
            Festival festival = FestivalFixture.festival()
                .startDate(entryTime.toLocalDate())
                .endDate(entryTime.plusDays(4).toLocalDate())
                .build();
            Stage stage = StageFixture.stage()
                .startTime(entryTime.plusHours(4))
                .festival(festival)
                .build();
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .stage(stage)
                .entryTime(entryTime)
                .build();

            // when & then
            assertThat(memberTicket.isCurrent(time)).isTrue();
        }

        @Test
        void 대기_상태면_참() {
            // given
            LocalDateTime entryTime = LocalDateTime.now();
            LocalDateTime time = entryTime.minusHours(12).plusSeconds(1);
            Festival festival = FestivalFixture.festival()
                .startDate(entryTime.toLocalDate())
                .endDate(entryTime.plusDays(4).toLocalDate())
                .build();
            Stage stage = StageFixture.stage()
                .startTime(entryTime.plusHours(4))
                .festival(festival)
                .build();
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .stage(stage)
                .entryTime(entryTime)
                .build();

            // when & then
            assertThat(memberTicket.isCurrent(time)).isTrue();
        }
    }

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
