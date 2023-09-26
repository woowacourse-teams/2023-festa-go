package com.festago.ticketing.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.BadRequestException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.stage.domain.Stage;
import com.festago.student.repository.StudentRepository;
import com.festago.support.MemberFixture;
import com.festago.support.TicketFixture;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.repository.TicketAmountRepository;
import com.festago.ticket.repository.TicketRepository;
import com.festago.ticketing.dto.TicketingRequest;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.time.Clock;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketingServiceTest {

    @Mock
    MemberTicketRepository memberTicketRepository;

    @Mock
    TicketAmountRepository ticketAmountRepository;

    @Mock
    TicketRepository ticketRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    StudentRepository studentRepository;

    @Spy
    Clock clock = Clock.systemDefaultZone();

    @InjectMocks
    TicketingService ticketingService;

    @Test
    void 재학생용_티켓인데_학생인증이_되지_않았으면_예외() {
        // given
        TicketingRequest request = new TicketingRequest(1L);
        given(memberRepository.findById(anyLong()))
            .willReturn(Optional.of(MemberFixture.member().build()));
        given(memberTicketRepository.existsByOwnerAndStage(any(Member.class), any(Stage.class)))
            .willReturn(false);

        given(ticketRepository.findByIdWithFetch(anyLong()))
            .willReturn(Optional.of(TicketFixture.ticket().ticketType(TicketType.STUDENT).build()));
        given(studentRepository.existsByMemberAndSchoolId(any(Member.class), anyLong()))
            .willReturn(false);

        // when & then
        assertThatThrownBy(() -> ticketingService.ticketing(1L, request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("학생 인증이 필요합니다.");
    }
}
