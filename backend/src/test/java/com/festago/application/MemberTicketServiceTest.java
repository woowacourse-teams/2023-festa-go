package com.festago.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import com.festago.exception.BadRequestException;
import com.festago.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.StageFixture;
import com.festago.ticketing.application.MemberTicketService;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.dto.MemberTicketResponse;
import com.festago.ticketing.dto.MemberTicketsResponse;
import com.festago.ticketing.repository.MemberTicketRepository;
import com.festago.zmember.domain.Member;
import com.festago.zmember.repository.MemberRepository;
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

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketServiceTest {

    @Mock
    MemberTicketRepository memberTicketRepository;

    @Mock
    MemberRepository memberRepository;

    @Spy
    Clock clock = Clock.systemDefaultZone();

    @InjectMocks
    MemberTicketService memberTicketService;

    @Nested
    class 사용자의_멤버티켓_전체_조회 {

        @Test
        void 멤버가_없으면_예외() {
            // given
            Long memberId = 1L;

            given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberTicketService.findAll(memberId, PageRequest.ofSize(1)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 멤버입니다.");
        }
    }

    @Nested
    class 현재_멤버티켓_조회 {

        @Test
        void 멤버가_없으면_예외() {
            // given
            Long memberId = 1L;

            given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberTicketService.findCurrent(memberId, Pageable.ofSize(10)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 멤버입니다.");
        }

        @Test
        void 입장시간이_24시간_지난_티켓은_조회되지_않는다() {
            // given
            Long memberId = 1L;
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .entryTime(LocalDateTime.now().minusHours(25))
                .build();

            given(memberTicketRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
                .willReturn(List.of(memberTicket));
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(new Member(memberId)));

            // when
            MemberTicketsResponse response = memberTicketService.findCurrent(memberId, Pageable.ofSize(100));

            // then
            assertThat(response.memberTickets()).isEmpty();
        }

        @Test
        void 활성화된_티켓이_먼저_조회된다() {
            // given
            Long memberId = 1L;
            MemberTicket pendingMemberTicket = MemberTicketFixture.memberTicket()
                .id(1L)
                .entryTime(LocalDateTime.now().plusHours(1))
                .build();
            MemberTicket activateMemberTicket = MemberTicketFixture.memberTicket()
                .id(2L)
                .entryTime(LocalDateTime.now().minusHours(1))
                .build();

            given(memberTicketRepository.findAllByOwnerId(eq(memberId), any(Pageable.class)))
                .willReturn(List.of(pendingMemberTicket, activateMemberTicket));
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(new Member(memberId)));

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
            MemberTicket pendingMemberTicket1 = MemberTicketFixture.memberTicket()
                .id(1L)
                .entryTime(LocalDateTime.now().plusHours(1))
                .build();
            MemberTicket pendingMemberTicket2 = MemberTicketFixture.memberTicket()
                .id(2L)
                .entryTime(LocalDateTime.now().plusHours(2))
                .build();
            MemberTicket activateMemberTicket1 = MemberTicketFixture.memberTicket()
                .id(3L)
                .entryTime(LocalDateTime.now().minusHours(2))
                .build();
            MemberTicket activateMemberTicket2 = MemberTicketFixture.memberTicket()
                .id(4L)
                .entryTime(LocalDateTime.now().minusHours(1))
                .build();

            given(memberTicketRepository.findAllByOwnerId(eq(memberId), any(Pageable.class)))
                .willReturn(
                    List.of(pendingMemberTicket1, pendingMemberTicket2, activateMemberTicket1, activateMemberTicket2));
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(new Member(memberId)));

            // when
            MemberTicketsResponse response = memberTicketService.findCurrent(memberId, Pageable.ofSize(100));

            // then
            List<Long> memberTicketIds = response.memberTickets().stream()
                .map(MemberTicketResponse::id)
                .toList();
            assertThat(memberTicketIds).containsExactly(4L, 3L, 1L, 2L);
        }
    }

    @Nested
    class 멤버_티켓_아이디로_단건_조회 {

        @Test
        void 멤버가_없으면_예외() {
            // given
            Long memberId = 1L;

            given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberTicketService.findById(memberId, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 멤버입니다.");
        }

        @Test
        void 사용자의_티켓이_없으면_예외() {
            // given
            Long memberId = 1L;
            Long memberTicketId = 1L;

            given(memberTicketRepository.findById(memberTicketId))
                .willReturn(Optional.empty());
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(new Member(memberId)));

            // when & then
            assertThatThrownBy(() -> memberTicketService.findById(memberId, memberTicketId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않은 멤버 티켓입니다.");
        }

        @Test
        void 사용자가_티켓의_주인이_아니면_예외() {
            // given
            Long memberId = 1L;
            Member other = MemberFixture.member()
                .id(2L)
                .build();

            MemberTicket otherMemberTicket = MemberTicketFixture.memberTicket()
                .owner(other)
                .build();

            Long otherTicketId = otherMemberTicket.getId();

            given(memberTicketRepository.findById(otherTicketId))
                .willReturn(Optional.of(otherMemberTicket));
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(new Member(memberId)));

            // when & then
            assertThatThrownBy(() -> memberTicketService.findById(memberId, otherTicketId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("해당 예매 티켓의 주인이 아닙니다.");
        }

        @Test
        void 성공() {
            // given
            Long memberId = 2L;
            Member member = MemberFixture.member()
                .id(memberId)
                .build();
            Stage stage = StageFixture.stage()
                .build();
            Long memberTicketId = 1L;
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .id(1L)
                .owner(member)
                .stage(stage)
                .build();

            given(memberTicketRepository.findById(memberTicketId))
                .willReturn(Optional.of(memberTicket));
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(new Member(memberId)));

            // when
            MemberTicketResponse response = memberTicketService.findById(member.getId(), memberTicketId);

            // then
            assertThat(response.id()).isEqualTo(memberTicketId);
        }
    }
}
