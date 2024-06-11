package com.festago.ticketing.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.spy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.festago.stage.domain.Stage;
import com.festago.support.TimeInstantProvider;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.support.fixture.StageTicketFixture;
import com.festago.ticket.domain.NewTicketType;
import com.festago.ticket.domain.StageTicket;
import com.festago.ticket.repository.MemoryStageTicketRepository;
import com.festago.ticket.repository.NewTicketDao;
import com.festago.ticket.repository.StageTicketRepository;
import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.dto.command.TicketingCommand;
import com.festago.ticketing.infrastructure.MemoryTicketingSequenceGenerator;
import com.festago.ticketing.repository.MemoryReserveTicketRepository;
import com.festago.ticketing.repository.ReserveTicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketingCommandServiceTest {

    TicketingCommandServiceImpl ticketingCommandService;

    ReserveTicketRepository reserveTicketRepository;

    StageTicketRepository stageTicketRepository;

    Clock clock;

    @BeforeEach
    void setUp() {
        reserveTicketRepository = new MemoryReserveTicketRepository();
        stageTicketRepository = new MemoryStageTicketRepository();
        clock = spy(Clock.systemDefaultZone());
        ticketingCommandService = new TicketingCommandServiceImpl(
            new NewTicketDao(stageTicketRepository),
            reserveTicketRepository,
            new MemoryTicketingSequenceGenerator(),
            Collections.emptyList(),
            clock
        );
    }

    @Nested
    class reserveTicket {

        StageTicket stageTicket;
        LocalDateTime 무대_시작_시간 = LocalDateTime.parse("2077-06-30T18:00:00");
        LocalDateTime 티켓_오픈_시간 = LocalDateTime.parse("2077-06-23T18:00:00");

        @BeforeEach
        void setUp() {
            School school = SchoolFixture.builder().id(1L).build();
            Festival festival = FestivalFixture.builder()
                .school(school)
                .startDate(무대_시작_시간.toLocalDate())
                .endDate(무대_시작_시간.toLocalDate())
                .build();
            Stage stage = StageFixture.builder()
                .festival(festival)
                .startTime(무대_시작_시간)
                .ticketOpenTime(티켓_오픈_시간)
                .build();
            stageTicket = stageTicketRepository.save(StageTicketFixture.builder()
                .schoolId(school.getId())
                .stage(stage)
                .build());
            stageTicket.addTicketEntryTime(school.getId(), 티켓_오픈_시간.minusHours(1), 무대_시작_시간.minusHours(1), 100);
            LocalDateTime now = LocalDateTime.parse("2077-06-24T18:00:00");
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
        }

        @Test
        void 티켓의_식별자에_해당하는_티켓이_없으면_예외() {
            // given
            var command = TicketingCommand.builder()
                .ticketId(4885L)
                .booker(new Booker(1L, 1L))
                .ticketType(NewTicketType.STAGE)
                .build();

            // when & then
            assertThatThrownBy(() -> ticketingCommandService.reserveTicket(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.TICKET_NOT_FOUND.getMessage());
        }

        @Test
        void 예매할_수_있는_티켓의_개수를_초과하면_예외() {
            // given
            var command = TicketingCommand.builder()
                .ticketId(stageTicket.getId())
                .booker(new Booker(1L, 1L))
                .ticketType(NewTicketType.STAGE)
                .build();
            ticketingCommandService.reserveTicket(command);

            // when & then
            assertThatThrownBy(() -> ticketingCommandService.reserveTicket(command))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.RESERVE_TICKET_OVER_AMOUNT.getMessage());
        }

        @Test
        void 티켓_예매에_성공하면_예매한_티켓이_영속된다() {
            // given
            var command = TicketingCommand.builder()
                .ticketId(stageTicket.getId())
                .booker(new Booker(1L, 1L))
                .ticketType(NewTicketType.STAGE)
                .build();

            // when
            ticketingCommandService.reserveTicket(command);

            // then
            assertThat(reserveTicketRepository.countByMemberIdAndTicketId(1L, stageTicket.getId()))
                .isEqualTo(1);
        }
    }
}
