package com.festago.application;

import static com.festago.common.exception.ErrorCode.MEMBER_TICKET_NOT_FOUND;
import static com.festago.common.exception.ErrorCode.NOT_ENOUGH_PERMISSION;
import static com.festago.common.exception.ErrorCode.NOT_ENTRY_TIME;
import static com.festago.common.exception.ErrorCode.NOT_MEMBER_TICKET_OWNER;
import static com.festago.common.exception.ErrorCode.STAFF_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ForbiddenException;
import com.festago.common.exception.NotFoundException;
import com.festago.entry.application.EntryCodeManager;
import com.festago.entry.application.EntryService;
import com.festago.entry.domain.EntryCode;
import com.festago.entry.domain.EntryCodePayload;
import com.festago.entry.dto.EntryCodeResponse;
import com.festago.entry.dto.TicketValidationRequest;
import com.festago.entry.dto.TicketValidationResponse;
import com.festago.entry.dto.event.EntryProcessEvent;
import com.festago.festival.domain.Festival;
import com.festago.member.domain.Member;
import com.festago.staff.domain.Staff;
import com.festago.staff.repository.StaffRepository;
import com.festago.stage.domain.Stage;
import com.festago.support.FestivalFixture;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.SetUpMockito;
import com.festago.support.StaffFixture;
import com.festago.support.StageFixture;
import com.festago.support.TimeInstantProvider;
import com.festago.ticketing.domain.EntryState;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EntryServiceTest {

    @Mock
    EntryCodeManager entryCodeManager;

    @Mock
    MemberTicketRepository memberTicketRepository;

    @Mock
    StaffRepository staffRepository;

    @Mock
    ApplicationEventPublisher publisher;

    @Spy
    Clock clock = Clock.systemDefaultZone();

    @InjectMocks
    EntryService entryService;

    @Nested
    class 티켓의_QR_생성_요청 {

        @Test
        void 입장_시간_전_요청하면_예외() {
            // given
            LocalDateTime entryTime = LocalDateTime.parse("2023-07-30T16:00:00");
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
                .id(1L)
                .stage(stage)
                .entryTime(entryTime)
                .build();
            Long memberId = memberTicket.getOwner().getId();
            Long memberTicketId = memberTicket.getId();
            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(entryTime.minusHours(1)));

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(memberId, memberTicketId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_ENTRY_TIME.getMessage());
        }

        @Test
        void 입장_시간이_24시간이_넘은_경우_예외() {
            // given
            LocalDateTime entryTime = LocalDateTime.parse("2023-07-30T16:00:00");
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
                .id(1L)
                .stage(stage)
                .entryTime(entryTime)
                .build();
            Long memberId = memberTicket.getOwner().getId();
            Long memberTicketId = memberTicket.getId();
            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));
            given(clock.instant())
                .willReturn(TimeInstantProvider.from((entryTime.plusHours(24))));

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(memberId, memberTicketId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_ENTRY_TIME.getMessage());
        }

        @Test
        void 자신의_티켓이_아니면_예외() {
            // given
            Long memberId = 1L;
            Member other = MemberFixture.member()
                .id(2L)
                .build();
            MemberTicket otherTicket = MemberTicketFixture.memberTicket()
                .id(1L)
                .owner(other)
                .build();
            Long memberTicketId = otherTicket.getId();

            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(otherTicket));

            // when & then
            assertThatThrownBy(() -> entryService.createEntryCode(memberId, memberTicketId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_MEMBER_TICKET_OWNER.getMessage());
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
                .hasMessage(MEMBER_TICKET_NOT_FOUND.getMessage());
        }

        @Test
        void 성공() {
            // given
            LocalDateTime entryTime = LocalDateTime.parse("2023-07-30T16:00:00");
            Instant now = Instant.from(ZonedDateTime.of(entryTime, ZoneId.systemDefault()));
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
                .id(1L)
                .stage(stage)
                .entryTime(entryTime)
                .build();
            EntryCode entryCode = new EntryCode("1234", 30, 10);
            Long memberId = memberTicket.getOwner().getId();
            Long memberTicketId = memberTicket.getId();

            given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));
            given(entryCodeManager.provide(any(EntryCodePayload.class), anyLong()))
                .willReturn(entryCode);
            given(clock.instant())
                .willReturn(now);

            // when
            EntryCodeResponse response = entryService.createEntryCode(memberId, memberTicketId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.code()).isEqualTo(entryCode.getCode());
                softly.assertThat(response.period()).isEqualTo(30);
            });
        }
    }

    @Nested
    class 티켓_검사 {

        TicketValidationRequest request;
        Long staffId;
        Long festivalId;
        MemberTicket memberTicket;

        @BeforeEach
        void setUp() {
            request = new TicketValidationRequest("code");
            staffId = 1L;
            festivalId = 2L;
            Festival festival = FestivalFixture.festival().id(festivalId).build();
            Staff staff = StaffFixture.staff().id(staffId).festival(festival).build();
            Stage stage = StageFixture.stage().festival(festival).build();
            memberTicket = MemberTicketFixture.memberTicket().id(1L).stage(stage).build();

            SetUpMockito
                .given(staffRepository.findById(staffId))
                .willReturn(Optional.of(staff));
            SetUpMockito
                .given(entryCodeManager.extract(anyString()))
                .willReturn(new EntryCodePayload(1L, EntryState.BEFORE_ENTRY));
            SetUpMockito
                .given(memberTicketRepository.findById(anyLong()))
                .willReturn(Optional.of(memberTicket));

        }

        @Test
        void 스태프가_없으면_예외_발생() {
            // given
            given(staffRepository.findById(staffId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> entryService.validate(request, staffId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(STAFF_NOT_FOUND.getMessage());
        }

        @Test
        void 해당_축제의_티켓이_아니면_권한없음_예외_발생() {
            // given
            festivalId = festivalId + 1L;
            Festival festival = FestivalFixture.festival().id(festivalId).build();
            Staff staff = StaffFixture.staff().id(staffId).festival(festival).build();

            given(staffRepository.findById(staffId))
                .willReturn(Optional.of(staff));

            // when & then
            assertThatThrownBy(() -> entryService.validate(request, staffId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(NOT_ENOUGH_PERMISSION.getMessage());
        }

        @Test
        void 예매한_티켓의_입장_상태와_요청의_입장_상태가_같으면_에매한_티켓의_입장_상태를_변경한다() {
            // when
            TicketValidationResponse expect = entryService.validate(request, staffId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(memberTicket.getEntryState()).isEqualTo(EntryState.AFTER_ENTRY);
                softly.assertThat(expect.updatedState()).isEqualTo(EntryState.AFTER_ENTRY);
            });
        }

        @Test
        void 예매한_티켓의_입장_상태와_요청의_입장_상태가_다르면_에매한_티켓의_입장_상태를_변경하지_않는다() {
            // given
            given(entryCodeManager.extract(anyString()))
                .willReturn(new EntryCodePayload(1L, EntryState.AFTER_ENTRY));

            // when
            TicketValidationResponse expect = entryService.validate(request, staffId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(memberTicket.getEntryState()).isEqualTo(EntryState.BEFORE_ENTRY);
                softly.assertThat(expect.updatedState()).isEqualTo(EntryState.BEFORE_ENTRY);
            });
            verify(publisher, times(1)).publishEvent(any(EntryProcessEvent.class));
        }
    }
}
