package com.festago.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.festago.domain.Member;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.Ticket;
import com.festago.dto.MemberTicketResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.NotFoundException;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
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
    void 사용자의_티켓_단건_조회() {
        // given
        Member member = MemberFixture.member()
            .id(2L)
            .build();
        Stage stage = StageFixture.stage()
            .build();
        Ticket ticket = TicketFixture.ticket()
            .stage(stage)
            .build();
        Long memberTicketId = 1L;
        MemberTicket memberTicket = MemberTicketFixture.memberTicket()
            .id(1L)
            .owner(member)
            .ticket(ticket)
            .build();

        given(memberTicketRepository.findById(memberTicketId))
            .willReturn(Optional.of(memberTicket));

        // when
        MemberTicketResponse response = memberTicketService.findById(member.getId(), memberTicketId);

        // then
        assertThat(response.id()).isEqualTo(memberTicketId);
    }
}
