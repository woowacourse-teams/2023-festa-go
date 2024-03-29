package com.festago.ticketing.application;

import static com.festago.common.exception.ErrorCode.MEMBER_TICKET_NOT_FOUND;
import static com.festago.common.exception.ErrorCode.NOT_MEMBER_TICKET_OWNER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.support.fixture.MemberFixture;
import com.festago.support.fixture.MemberTicketFixture;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.dto.MemberTicketResponse;
import com.festago.ticketing.dto.MemberTicketsResponse;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MemberTicketServiceTest {

    @Mock
    MemberTicketRepository memberTicketRepository;

    @Spy
    Clock clock = Clock.systemDefaultZone();

    @InjectMocks
    MemberTicketService memberTicketService;

    @Nested
    class 멤버_티켓_아이디로_단건_조회 {

        @Test
        void 멤버_티켓이_없으면_예외() {
            // given
            Long memberId = 1L;
            Long memberTicketId = 1L;

            given(memberTicketRepository.findById(memberTicketId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberTicketService.findById(memberId, memberTicketId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MEMBER_TICKET_NOT_FOUND.getMessage());
        }

        @Test
        void 사용자가_티켓의_주인이_아니면_예외() {
            // given
            Long memberId = 1L;
            Long memberTicketId = 1L;
            Member other = MemberFixture.builder()
                .id(2L)
                .build();

            MemberTicket otherMemberTicket = MemberTicketFixture.builder()
                .id(memberTicketId)
                .owner(other)
                .build();

            given(memberTicketRepository.findById(memberTicketId))
                .willReturn(Optional.of(otherMemberTicket));

            // when & then
            assertThatThrownBy(() -> memberTicketService.findById(memberId, memberTicketId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_MEMBER_TICKET_OWNER.getMessage());
        }

        @Test
        void 성공() {
            // given
            Long memberId = 1L;
            Long memberTicketId = 1L;
            Member member = MemberFixture.builder()
                .id(memberId)
                .build();
            MemberTicket memberTicket = MemberTicketFixture.builder()
                .id(memberTicketId)
                .owner(member)
                .build();

            given(memberTicketRepository.findById(memberTicketId))
                .willReturn(Optional.of(memberTicket));

            // when
            MemberTicketResponse response = memberTicketService.findById(memberId, memberTicketId);

            // then
            assertThat(response.id()).isEqualTo(memberTicketId);
        }
    }

    @Nested
    class 사용자의_멤버티켓_전체_조회 {

        @Test
        void 멤버_티켓이_없으면_빈_리스트() {
            // given
            Long memberId = 1L;

            // when
            MemberTicketsResponse response = memberTicketService.findAll(memberId, PageRequest.ofSize(1));

            // then
            assertThat(response.memberTickets()).isEmpty();
        }

        @Test
        void 성공() {
            // given
            Long memberId = 1L;
            MemberTicket first = MemberTicketFixture.builder()
                .id(1L)
                .build();
            MemberTicket second = MemberTicketFixture.builder()
                .id(2L)
                .build();
            given(memberTicketRepository.findAllByOwnerId(eq(memberId), any(Pageable.class)))
                .willReturn(List.of(first, second));

            // when
            MemberTicketsResponse response = memberTicketService.findAll(memberId, PageRequest.ofSize(1));

            // then
            assertThat(response.memberTickets()).hasSize(2);
        }
    }

    @Nested
    class 현재_멤버티켓_조회 {

        @Test
        void 입장시간이_24시간_지난_티켓은_조회되지_않는다() {
            // given
            Long memberId = 1L;
            MemberTicket memberTicket = MemberTicketFixture.builder()
                .entryTime(LocalDateTime.now().minusHours(25))
                .build();

            given(memberTicketRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
                .willReturn(List.of(memberTicket));

            // when
            MemberTicketsResponse response = memberTicketService.findCurrent(memberId, Pageable.ofSize(100));

            // then
            assertThat(response.memberTickets()).isEmpty();
        }

        @Test
        void 활성화된_티켓이_먼저_조회된다() {
            // given
            Long memberId = 1L;
            MemberTicket pendingMemberTicket = MemberTicketFixture.builder()
                .id(1L)
                .entryTime(LocalDateTime.now().plusHours(1))
                .build();
            MemberTicket activateMemberTicket = MemberTicketFixture.builder()
                .id(2L)
                .entryTime(LocalDateTime.now().minusHours(1))
                .build();

            given(memberTicketRepository.findAllByOwnerId(eq(memberId), any(Pageable.class)))
                .willReturn(List.of(pendingMemberTicket, activateMemberTicket));

            // when
            MemberTicketsResponse response = memberTicketService.findCurrent(memberId, Pageable.ofSize(100));

            // then
            List<Long> memberTicketIds = response.memberTickets().stream()
                .map(MemberTicketResponse::id)
                .toList();
            assertThat(memberTicketIds).containsExactly(2L, 1L);
        }

        @Test
        void 활성화_및_비활성화_내에서는_현재시간과_가까운순으로_정렬되어_조회된다() {
            // given
            Long memberId = 1L;
            MemberTicket pendingMemberTicket1 = MemberTicketFixture.builder()
                .id(1L)
                .entryTime(LocalDateTime.now().plusHours(1))
                .build();
            MemberTicket pendingMemberTicket2 = MemberTicketFixture.builder()
                .id(2L)
                .entryTime(LocalDateTime.now().plusHours(2))
                .build();
            MemberTicket activateMemberTicket1 = MemberTicketFixture.builder()
                .id(3L)
                .entryTime(LocalDateTime.now().minusHours(2))
                .build();
            MemberTicket activateMemberTicket2 = MemberTicketFixture.builder()
                .id(4L)
                .entryTime(LocalDateTime.now().minusHours(1))
                .build();

            given(memberTicketRepository.findAllByOwnerId(eq(memberId), any(Pageable.class)))
                .willReturn(
                    List.of(pendingMemberTicket1, pendingMemberTicket2, activateMemberTicket1, activateMemberTicket2));

            // when
            MemberTicketsResponse response = memberTicketService.findCurrent(memberId, Pageable.ofSize(100));

            // then
            List<Long> memberTicketIds = response.memberTickets().stream()
                .map(MemberTicketResponse::id)
                .toList();
            assertThat(memberTicketIds).containsExactly(4L, 3L, 1L, 2L);
        }
    }
}
