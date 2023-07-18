package com.festago.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import com.festago.domain.EntryCodeProvider;
import com.festago.domain.Member;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Ticket;
import com.festago.dto.EntryCodeResponse;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
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
            MemberTicket memberTicket = new MemberTicket(1L, new Member(1L), new Ticket(entryTime));

            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 자신의_티켓이_아니면_예외() {
            // given
            LocalDateTime entryTime = LocalDateTime.now().minusMinutes(30);
            long ownerId = 1L;
            long memberId = 2L;
            MemberTicket memberTicket = new MemberTicket(1L, new Member(ownerId), new Ticket(entryTime));

            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(memberId, 1L))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않은_티켓이면_예외() {
            // given
            long memberTicketId = 1L;
            given(memberTicketRepository.findById(memberTicketId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(1L, memberTicketId))
                .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void 입장_시간이_24시간이_넘은_경우_예외() {
            // given
            LocalDateTime entryTime = LocalDateTime.now().minusDays(1).minusSeconds(10);
            MemberTicket memberTicket = new MemberTicket(1L, new Member(1L), new Ticket(entryTime));

            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공() {
            // given
            MemberTicket memberTicket = new MemberTicket(1L, new Member(1L), new Ticket(LocalDateTime.now()));
            String code = "3112321312123";
            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));
            given(entryCodeProvider.provide(any(), any()))
                .willReturn(code);

            // when
            EntryCodeResponse entryCode = entryService.createEntryCode(1L, 1L);

            // then
            assertSoftly(softAssertions -> {
                assertThat(entryCode.code()).isEqualTo(code);
                assertThat(entryCode.period()).isEqualTo(30);
            });
        }
    }
}
