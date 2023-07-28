package com.festago.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import com.festago.domain.EntryCodeProvider;
import com.festago.domain.Festival;
import com.festago.domain.Member;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.Ticket;
import com.festago.dto.EntryCodeResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.NotFoundException;
import com.festago.support.FestivalFixture;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
import java.time.LocalDateTime;
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
class EntryServiceTest {

    @Mock
    EntryCodeProvider entryCodeProvider;

    @Mock
    MemberTicketRepository memberTicketRepository;

    @InjectMocks
    EntryService entryService;

    @Nested
    class 티켓의_QR_생성_요청 {

        @Test
        void 입장_시간_전_요청하면_예외() {
            // given
            LocalDateTime entryTime = LocalDateTime.now().plusMinutes(30);
            Stage stage = StageFixture.stage()
                .startTime(LocalDateTime.now().plusHours(1))
                .build();
            Ticket ticket = TicketFixture.ticket()
                .stage(stage)
                .entryTime(entryTime)
                .build();
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .id(1L)
                .ticket(ticket)
                .build();
            Long memberId = memberTicket.getOwner().getId();
            Long memberTicketId = memberTicket.getId();

            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(memberId, memberTicketId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("입장 가능한 시간이 아닙니다.");
        }

        @Test
        void 입장_시간이_24시간이_넘은_경우_예외() {
            // given
            LocalDateTime stageStartTime = LocalDateTime.now().minusHours(24);
            LocalDateTime entryTime = stageStartTime.minusSeconds(10);
            Festival festival = FestivalFixture.festival()
                .startDate(stageStartTime.toLocalDate())
                .endDate(stageStartTime.toLocalDate())
                .build();
            Stage stage = StageFixture.stage()
                .festival(festival)
                .startTime(stageStartTime)
                .build();
            Ticket ticket = TicketFixture.ticket()
                .stage(stage)
                .entryTime(entryTime)
                .build();
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .id(1L)
                .ticket(ticket)
                .build();
            Long memberId = memberTicket.getOwner().getId();
            Long memberTicketId = memberTicket.getId();

            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(memberId, memberTicketId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("입장 가능한 시간이 아닙니다.");
        }

        @Test
        void 자신의_티켓이_아니면_예외() {
            // given
            LocalDateTime entryTime = LocalDateTime.now().minusMinutes(30);
            Long memberId = 1L;
            Member other = MemberFixture.member()
                .id(2L)
                .build();
            Ticket ticket = TicketFixture.ticket()
                .entryTime(entryTime)
                .build();
            MemberTicket otherTicket = MemberTicketFixture.memberTicket()
                .id(1L)
                .owner(other)
                .ticket(ticket)
                .build();
            Long memberTicketId = otherTicket.getId();

            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(otherTicket));

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(memberId, memberTicketId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("해당 예매 티켓의 주인이 아닙니다.");
        }

        @Test
        void 존재하지_않은_티켓이면_예외() {
            // given
            Long memberTicketId = 1L;
            given(memberTicketRepository.findById(memberTicketId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(1L, memberTicketId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않은 멤버 티켓입니다.");
        }

        @Test
        void 성공() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .id(1L)
                .build();
            String code = "3112321312123";
            Long memberId = memberTicket.getOwner().getId();
            Long memberTicketId = memberTicket.getId();

            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));
            given(entryCodeProvider.provide(any(), any()))
                .willReturn(code);

            // when
            EntryCodeResponse entryCode = entryService.createEntryCode(memberId, memberTicketId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(entryCode.code()).isEqualTo(code);
                softly.assertThat(entryCode.period()).isEqualTo(30);
            });
        }
    }
}
