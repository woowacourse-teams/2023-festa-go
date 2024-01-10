package com.festago.ticketing.application;

import static com.festago.common.exception.ErrorCode.MEMBER_TICKET_NOT_FOUND;
import static com.festago.common.exception.ErrorCode.NOT_MEMBER_TICKET_OWNER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.dto.MemberTicketResponse;
import com.festago.ticketing.dto.MemberTicketsResponse;
import com.festago.ticketing.repository.MemoryMemberTicketRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketServiceTest {

    Member member;

    MemoryMemberTicketRepository memberTicketRepository = new MemoryMemberTicketRepository();

    Clock clock = spy(Clock.systemDefaultZone());

    MemberTicketService memberTicketService = new MemberTicketService(
        memberTicketRepository,
        clock
    );

    @BeforeEach
    void setUp() {
        member = MemberFixture.member().id(1L).build();
        memberTicketRepository.clear();
        reset(clock);
    }

    // 눈에 띄기 위해 고의적으로 위에 배치함
    private MemberTicket createPersistMemberTicket(Member owner) {
        return createPersistMemberTicket(owner, LocalDateTime.now(clock));
    }

    private MemberTicket createPersistMemberTicket(Member owner, LocalDateTime entryTime) {
        MemberTicket memberTicket = MemberTicketFixture.memberTicket()
            .owner(owner)
            .entryTime(entryTime)
            .build();
        memberTicketRepository.save(memberTicket);
        return memberTicket;
    }

    @Nested
    class 멤버_티켓_아이디로_단건_조회 {

        @Nested
        class 실패 {

            @Test
            void 멤버_티켓이_없으면_예외() {
                // given
                Long memberId = 1L;
                Long memberTicketId = 1L;

                // when & then
                assertThatThrownBy(() -> memberTicketService.findById(memberId, memberTicketId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(MEMBER_TICKET_NOT_FOUND.getMessage());
            }

            @Test
            void 사용자가_티켓의_주인이_아니면_예외() {
                // given
                Long memberId = member.getId();
                Member otherMember = MemberFixture.member()
                    .id(memberId + 1)
                    .build();
                MemberTicket otherMemberTicket = createPersistMemberTicket(otherMember);
                Long otherMemberTicketId = otherMemberTicket.getId();

                // when & then
                assertThatThrownBy(() -> memberTicketService.findById(memberId, otherMemberTicketId))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(NOT_MEMBER_TICKET_OWNER.getMessage());
            }
        }

        @Nested
        class 성공 {

            @Test
            void 티켓의_주인이면_성공() {
                // given
                Long memberId = member.getId();
                MemberTicket memberTicket = createPersistMemberTicket(member);
                Long memberTicketId = memberTicket.getId();

                // when
                MemberTicketResponse response = memberTicketService.findById(memberId, memberTicketId);

                // then
                assertThat(response.id()).isEqualTo(memberTicketId);
            }
        }
    }

    @Nested
    class 사용자의_멤버티켓_전체_조회 {

        @Test
        void 멤버_티켓이_없으면_빈_리스트() {
            // given
            Long memberId = member.getId();

            // when
            MemberTicketsResponse response = memberTicketService.findAll(memberId, PageRequest.ofSize(1));

            // then
            assertThat(response.memberTickets()).isEmpty();
        }

        @Test
        void 성공() {
            // given
            Long memberId = member.getId();
            MemberTicket firstMemberTicket = createPersistMemberTicket(member);
            MemberTicket secondMemberTicket = createPersistMemberTicket(member);

            // when
            MemberTicketsResponse response = memberTicketService.findAll(memberId, PageRequest.ofSize(1));

            // then
            assertThat(response.memberTickets())
                .map(MemberTicketResponse::id)
                .contains(firstMemberTicket.getId(), secondMemberTicket.getId())
                .hasSize(2);
        }
    }

    @Nested
    class 현재_멤버티켓_조회 {

        @Test
        void 입장시간이_24시간_지난_티켓은_조회되지_않는다() {
            // given
            Long memberId = member.getId();
            doReturn(Instant.parse("2023-01-10T17:00:00Z"))
                .when(clock)
                .instant();
            createPersistMemberTicket(member, LocalDateTime.now(clock).minusDays(1));

            // when
            MemberTicketsResponse response = memberTicketService.findCurrent(memberId, Pageable.ofSize(100));

            // then
            assertThat(response.memberTickets()).isEmpty();
        }

        @Test
        void 입장시간이_24시간_이내인_티켓은_조회된다() {
            // given
            Long memberId = member.getId();
            doReturn(Instant.parse("2023-01-10T17:00:00Z"))
                .when(clock)
                .instant();
            LocalDateTime now = LocalDateTime.now(clock);

            MemberTicket memberTicket = createPersistMemberTicket(member, now.minusDays(1).plusSeconds(1));

            // when
            MemberTicketsResponse response = memberTicketService.findCurrent(memberId, Pageable.ofSize(100));

            // then
            assertThat(response.memberTickets())
                .map(MemberTicketResponse::id)
                .contains(memberTicket.getId())
                .hasSize(1);
        }

        @Test
        void 활성화된_티켓이_먼저_조회된다() {
            // given
            Long memberId = member.getId();
            doReturn(Instant.parse("2023-01-10T17:00:00Z"))
                .when(clock)
                .instant();
            LocalDateTime now = LocalDateTime.now(clock);

            MemberTicket pendingMemberTicket = createPersistMemberTicket(member, now.plusSeconds(1));
            MemberTicket activateMemberTicket = createPersistMemberTicket(member, now.minusSeconds(1));

            // when
            MemberTicketsResponse response = memberTicketService.findCurrent(memberId, Pageable.ofSize(100));

            // then
            assertThat(response.memberTickets())
                .map(MemberTicketResponse::id)
                .containsExactly(activateMemberTicket.getId(), pendingMemberTicket.getId());
        }

        @Test
        void 활성화_및_비활성화_내에서는_현재시간과_가까운순으로_정렬되어_조회된다() {
            // given
            Long memberId = member.getId();
            doReturn(Instant.parse("2023-01-10T17:00:00Z"))
                .when(clock)
                .instant();
            LocalDateTime now = LocalDateTime.now(clock);

            MemberTicket firstPendingMemberTicket = createPersistMemberTicket(member, now.plusHours(1));
            MemberTicket secondPendingMemberTicket = createPersistMemberTicket(member, now.plusHours(2));
            MemberTicket firstActivateMemberTicket = createPersistMemberTicket(member, now.minusHours(1));
            MemberTicket secondActivateMemberTicket = createPersistMemberTicket(member, now.minusHours(2));

            // when
            MemberTicketsResponse response = memberTicketService.findCurrent(memberId, Pageable.ofSize(100));

            // then
            assertThat(response.memberTickets())
                .map(MemberTicketResponse::id)
                .containsExactly(
                    firstActivateMemberTicket.getId(),
                    secondActivateMemberTicket.getId(),
                    firstPendingMemberTicket.getId(),
                    secondPendingMemberTicket.getId()
                );
        }
    }
}
