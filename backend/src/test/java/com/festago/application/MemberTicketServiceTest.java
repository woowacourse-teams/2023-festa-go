package com.festago.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.festago.domain.Member;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.dto.CurrentMemberTicketResponse;
import com.festago.dto.CurrentMemberTicketsResponse;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.NotFoundException;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.StageFixture;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTicketServiceTest {

    @Mock
    MemberTicketRepository memberTicketRepository;

    @InjectMocks
    MemberTicketService memberTicketService;

    @Test
    void 사용자의_멤버티켓_전체_조회시_공연시작시간이_빠른순으로_정렬된다() {
        // given
        long memberId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Stage stage1 = StageFixture.stage().build();
        Stage stage2 = StageFixture.stage().startTime(now.plusDays(1)).build();
        Stage stage3 = StageFixture.stage().startTime(now.plusDays(2)).build();
        MemberTicket memberTicket1 = MemberTicketFixture.memberTicket()
            .id(1L)
            .entryTime(stage1.getStartTime().minusHours(1))
            .build();
        MemberTicket memberTicket2 = MemberTicketFixture.memberTicket()
            .id(2L)
            .entryTime(stage2.getStartTime().minusHours(1))
            .build();
        MemberTicket memberTicket3 = MemberTicketFixture.memberTicket()
            .id(3L)
            .entryTime(stage3.getStartTime().minusHours(1))
            .build();

        given(memberTicketRepository.findAllByOwnerId(memberId))
            .willReturn(List.of(memberTicket2, memberTicket1, memberTicket3));

        // when
        MemberTicketsResponse response = memberTicketService.findAll(memberId);

        // then
        List<Long> memberTicketIds = response.memberTickets().stream()
            .map(MemberTicketResponse::id)
            .toList();
        assertThat(memberTicketIds).containsExactly(1L, 2L, 3L);
    }

    @Nested
    class 현재_멤버티켓_조회 {

        @Test
        void 입장시간이_12시간이상_남은_티켓은_조회되지_않는다() {
            // given
            long memberId = 1L;
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .entryTime(LocalDateTime.now().plusHours(13))
                .build();

            given(memberTicketRepository.findAllByOwnerId(memberId))
                .willReturn(List.of(memberTicket));

            // when
            CurrentMemberTicketsResponse response = memberTicketService.findCurrent(memberId);

            // then
            assertThat(response.memberTickets()).isEmpty();
        }

        @Test
        void 입장시간이_24시간_지난_티켓은_조회되지_않는다() {
            // given
            long memberId = 1L;
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .entryTime(LocalDateTime.now().minusHours(25))
                .build();

            given(memberTicketRepository.findAllByOwnerId(memberId))
                .willReturn(List.of(memberTicket));

            // when
            CurrentMemberTicketsResponse response = memberTicketService.findCurrent(memberId);

            // then
            assertThat(response.memberTickets()).isEmpty();
        }

        @Test
        void 활성화된_티켓이_먼저_조회된다() {
            // given
            long memberId = 1L;
            MemberTicket pendingMemberTicket = MemberTicketFixture.memberTicket()
                .id(1L)
                .entryTime(LocalDateTime.now().plusHours(1))
                .build();
            MemberTicket activateMemberTicket = MemberTicketFixture.memberTicket()
                .id(2L)
                .entryTime(LocalDateTime.now().minusHours(1))
                .build();

            given(memberTicketRepository.findAllByOwnerId(memberId))
                .willReturn(List.of(pendingMemberTicket, activateMemberTicket));

            // when
            CurrentMemberTicketsResponse response = memberTicketService.findCurrent(memberId);

            // then
            List<Long> memberTicketIds = response.memberTickets().stream()
                .map(CurrentMemberTicketResponse::id)
                .toList();
            assertThat(memberTicketIds).containsExactly(2L, 1L);
        }

        @Test
        void 활성화_및_비활성화_내에서는_현재시간과_가까운순으로_정렬되어_조회된다() {
            // given
            long memberId = 1L;
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

            given(memberTicketRepository.findAllByOwnerId(memberId))
                .willReturn(
                    List.of(pendingMemberTicket1, pendingMemberTicket2, activateMemberTicket1, activateMemberTicket2));

            // when
            CurrentMemberTicketsResponse response = memberTicketService.findCurrent(memberId);

            // then
            List<Long> memberTicketIds = response.memberTickets().stream()
                .map(CurrentMemberTicketResponse::id)
                .toList();
            assertThat(memberTicketIds).containsExactly(4L, 3L, 1L, 2L);
        }
    }

    @Nested
    class 멤버_티켓_아이디로_단건_조회 {

        @Test
        void 사용자의_티켓이_없으면_예외() {
            // given
            Long memberId = 1L;
            Long memberTicketId = 1L;
            given(memberTicketRepository.findById(memberTicketId))
                .willReturn(Optional.empty());

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

            // when & then
            assertThatThrownBy(() -> memberTicketService.findById(memberId, otherTicketId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("해당 예매 티켓의 주인이 아닙니다.");
        }

        @Test
        void 성공() {
            // given
            Member member = MemberFixture.member()
                .id(2L)
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

            // when
            MemberTicketResponse response = memberTicketService.findById(member.getId(), memberTicketId);

            // then
            assertThat(response.id()).isEqualTo(memberTicketId);
        }
    }
}
