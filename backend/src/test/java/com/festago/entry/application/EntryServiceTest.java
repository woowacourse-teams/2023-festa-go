package com.festago.entry.application;

import static com.festago.common.exception.ErrorCode.MEMBER_TICKET_NOT_FOUND;
import static com.festago.common.exception.ErrorCode.NOT_ENTRY_TIME;
import static com.festago.common.exception.ErrorCode.NOT_MEMBER_TICKET_OWNER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.entry.domain.EntryCode;
import com.festago.entry.domain.EntryCodePayload;
import com.festago.entry.dto.EntryCodeResponse;
import com.festago.entry.dto.TicketValidationRequest;
import com.festago.entry.dto.TicketValidationResponse;
import com.festago.entry.dto.event.EntryProcessEvent;
import com.festago.entry.infrastructure.JsonEntryCodeExtractor;
import com.festago.entry.infrastructure.JsonEntryCodeProvider;
import com.festago.festival.domain.Festival;
import com.festago.member.domain.Member;
import com.festago.stage.domain.Stage;
import com.festago.support.FestivalFixture;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.StageFixture;
import com.festago.ticketing.domain.EntryState;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.repository.MemoryMemberTicketRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EntryServiceTest {

    EntryCodeManager entryCodeManager = new EntryCodeManager(
        new JsonEntryCodeProvider(),
        new JsonEntryCodeExtractor()
    );

    MemoryMemberTicketRepository memberTicketRepository = new MemoryMemberTicketRepository();

    ApplicationEventPublisher eventPublisher = mock();

    Clock clock = spy(Clock.systemDefaultZone());

    EntryService entryService = new EntryService(
        entryCodeManager,
        memberTicketRepository,
        eventPublisher,
        clock
    );

    @BeforeEach
    void setUp() {
        memberTicketRepository.clear();
        reset(eventPublisher, clock);
    }

    @Nested
    class 티켓의_QR_생성_요청 {

        Member member;

        @BeforeEach
        void setUp() {
            member = MemberFixture.member().id(1L).build();
        }

        @Test
        void 존재하지_않은_티켓이면_예외() {
            // given
            Long memberTicketId = 1L;

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(member.getId(), memberTicketId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MEMBER_TICKET_NOT_FOUND.getMessage());
        }

        @Test
        void 입장_시간_전_요청하면_예외() {
            // given
            doReturn(Instant.parse("2023-07-30T16:00:00Z"))
                .when(clock)
                .instant();
            LocalDateTime now = LocalDateTime.now(clock);

            LocalDateTime entryTime = now.plusDays(1);
            Festival festival = FestivalFixture.festival()
                .startDate(entryTime.toLocalDate())
                .endDate(entryTime.toLocalDate())
                .build();

            Stage stage = StageFixture.stage()
                .festival(festival)
                .startTime(entryTime.plusHours(2))
                .ticketOpenTime(entryTime.minusHours(1))
                .build();

            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .owner(member)
                .stage(stage)
                .entryTime(entryTime)
                .build();
            memberTicketRepository.save(memberTicket);

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(member.getId(), memberTicket.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_ENTRY_TIME.getMessage());
        }

        @Test
        void 입장_시간이_24시간이_넘은_경우_예외() {
            // given
            doReturn(Instant.parse("2023-07-30T16:00:00Z"))
                .when(clock)
                .instant();
            LocalDateTime now = LocalDateTime.now(clock);
            LocalDateTime entryTime = now.minusDays(1);

            Festival festival = FestivalFixture.festival()
                .startDate(entryTime.toLocalDate())
                .endDate(entryTime.toLocalDate())
                .build();

            Stage stage = StageFixture.stage()
                .festival(festival)
                .startTime(entryTime.plusHours(2))
                .ticketOpenTime(entryTime.minusHours(1))
                .build();

            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .owner(member)
                .stage(stage)
                .entryTime(entryTime)
                .build();
            memberTicketRepository.save(memberTicket);

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(member.getId(), memberTicket.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_ENTRY_TIME.getMessage());
        }

        @Test
        void 자신의_티켓이_아니면_예외() {
            // given
            Member otherMember = MemberFixture.member()
                .id(member.getId() + 1)
                .build();

            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .owner(otherMember)
                .build();
            memberTicketRepository.save(memberTicket);

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(member.getId(), memberTicket.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_MEMBER_TICKET_OWNER.getMessage());
        }

        @Test
        void 성공() {
            // given
            doReturn(Instant.parse("2023-07-30T16:00:00Z"))
                .when(clock)
                .instant();
            LocalDateTime now = LocalDateTime.now(clock);

            Festival festival = FestivalFixture.festival()
                .startDate(now.toLocalDate())
                .endDate(now.toLocalDate())
                .build();

            Stage stage = StageFixture.stage()
                .festival(festival)
                .startTime(now.plusHours(2))
                .ticketOpenTime(now.minusDays(1))
                .build();

            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .owner(member)
                .stage(stage)
                .entryTime(now.minusHours(1))
                .build();
            memberTicketRepository.save(memberTicket);

            // when
            EntryCodeResponse response = entryService.createEntryCode(member.getId(), memberTicket.getId());

            // then
            EntryCodePayload entryCodePayload = entryCodeManager.extract(response.code());
            assertThat(entryCodePayload.getMemberTicketId()).isEqualTo(memberTicket.getId());
        }
    }

    @Nested
    class 티켓_검사 {

        Member member;

        @BeforeEach
        void setUp() {
            member = MemberFixture.member().id(1L).build();
        }

        @Test
        void 예매한_티켓의_입장_상태와_요청의_입장_상태가_같으면_에매한_티켓의_입장_상태를_변경한다() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .owner(member)
                .build();
            memberTicketRepository.save(memberTicket);

            EntryCodePayload entryCodePayload = new EntryCodePayload(memberTicket.getId(),
                memberTicket.getEntryState());
            EntryCode code = entryCodeManager.provide(entryCodePayload, clock.millis());
            TicketValidationRequest request = new TicketValidationRequest(code.getCode());

            // when
            TicketValidationResponse expect = entryService.validate(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(memberTicket.getEntryState()).isEqualTo(EntryState.AFTER_ENTRY);
                softly.assertThat(expect.updatedState()).isEqualTo(EntryState.AFTER_ENTRY);
            });
            verify(eventPublisher, times(1)).publishEvent(any(EntryProcessEvent.class));
        }

        @Test
        void 예매한_티켓의_입장_상태와_요청의_입장_상태가_다르면_에매한_티켓의_입장_상태를_변경하지_않는다() {
            // given
            MemberTicket memberTicket = MemberTicketFixture.memberTicket()
                .build();
            memberTicketRepository.save(memberTicket);

            EntryCodePayload entryCodePayload = new EntryCodePayload(memberTicket.getId(), EntryState.AFTER_ENTRY);
            EntryCode code = entryCodeManager.provide(entryCodePayload, clock.millis());
            TicketValidationRequest request = new TicketValidationRequest(code.getCode());

            // when
            TicketValidationResponse expect = entryService.validate(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(memberTicket.getEntryState()).isEqualTo(EntryState.BEFORE_ENTRY);
                softly.assertThat(expect.updatedState()).isEqualTo(EntryState.BEFORE_ENTRY);
            });
            verify(eventPublisher, times(1)).publishEvent(any(EntryProcessEvent.class));
        }
    }
}
