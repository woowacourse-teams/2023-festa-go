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
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
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
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 사용자가_티켓의_주인이_아니면_예외() {
        // given
        Long memberId = 1L;
        Long memberTicketId = 1L;
        Member other = new Member(2L);
        Stage stage = new Stage(null, "테코대학교 축제", LocalDateTime.now());
        Ticket ticket = new Ticket(null, stage, LocalDateTime.now());
        MemberTicket memberTicket = new MemberTicket(memberTicketId, other, ticket);
        given(memberTicketRepository.findById(memberTicketId))
            .willReturn(Optional.of(memberTicket));

        // when & then
        assertThatThrownBy(() -> memberTicketService.findById(memberId, memberTicketId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사용자의_티켓_단건_조회() {
        // given
        Long memberTicketId = 1L;
        Member member = new Member(2L);
        Stage stage = new Stage(null, "테코대학교 축제", LocalDateTime.now());
        Ticket ticket = new Ticket(null, stage, LocalDateTime.now());
        MemberTicket memberTicket = new MemberTicket(memberTicketId, member, ticket);
        given(memberTicketRepository.findById(memberTicketId))
            .willReturn(Optional.of(memberTicket));

        // when
        MemberTicketResponse response = memberTicketService.findById(member.getId(), memberTicketId);

        // then
        assertThat(response.id()).isEqualTo(memberTicketId);
    }
}
