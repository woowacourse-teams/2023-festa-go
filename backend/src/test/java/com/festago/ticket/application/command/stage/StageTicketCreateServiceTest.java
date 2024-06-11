package com.festago.ticket.application.command.stage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.spy;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.festival.repository.MemoryFestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.MemorySchoolRepository;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.TimeInstantProvider;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.ticket.domain.NewTicket;
import com.festago.ticket.domain.TicketExclusive;
import com.festago.ticket.dto.command.StageTicketCreateCommand;
import com.festago.ticket.repository.MemoryStageTicketRepository;
import com.festago.ticket.repository.StageTicketRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageTicketCreateServiceTest {

    StageTicketCreateService stageTicketCreateService;

    SchoolRepository schoolRepository;

    FestivalRepository festivalRepository;

    StageRepository stageRepository;

    StageTicketRepository stageTicketRepository;

    Clock clock;

    @BeforeEach
    void setUp() {
        clock = spy(Clock.systemDefaultZone());
        schoolRepository = new MemorySchoolRepository();
        festivalRepository = new MemoryFestivalRepository();
        stageRepository = new MemoryStageRepository();
        stageTicketRepository = new MemoryStageTicketRepository();
        stageTicketCreateService = new StageTicketCreateService(
            stageTicketRepository,
            stageRepository,
            mock(ApplicationEventPublisher.class),
            clock
        );
    }

    @Nested
    class createStageTicket {

        School 테코대학교;
        Festival 테코대학교_축제;
        Stage 테코대학교_1일차_공연;
        LocalDate _6월_15일 = LocalDate.parse("2077-06-15");
        LocalDateTime _6월_7일_18시_0분 = LocalDateTime.parse("2077-06-07T18:00:00");
        LocalDateTime _6월_8일_18시_0분 = LocalDateTime.parse("2077-06-08T18:00:00");
        LocalDateTime _6월_15일_17시_0분 = LocalDateTime.parse("2077-06-15T17:00:00");
        LocalDateTime _6월_15일_18시_0분 = LocalDateTime.parse("2077-06-15T18:00:00");

        @BeforeEach
        void setUp() {
            테코대학교 = schoolRepository.save(SchoolFixture.builder().build());
            테코대학교_축제 = festivalRepository.save(
                FestivalFixture.builder().school(테코대학교).startDate(_6월_15일).endDate(_6월_15일).build());
            테코대학교_1일차_공연 = stageRepository.save(
                StageFixture.builder().festival(테코대학교_축제).startTime(_6월_15일_18시_0분).ticketOpenTime(_6월_8일_18시_0분)
                    .build());
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(_6월_7일_18시_0분));
        }

        @Test
        void 기존_StageTicket이_존재하지_않으면_새로운_StageTicket이_생성된다() {
            // given
            var command = StageTicketCreateCommand.builder()
                .schoolId(테코대학교.getId())
                .stageId(테코대학교_1일차_공연.getId())
                .ticketExclusive(TicketExclusive.STUDENT)
                .amount(100)
                .entryTime(_6월_15일_17시_0분)
                .build();

            // when
            Long stageTicketId = stageTicketCreateService.createStageTicket(command);

            // then
            assertThat(stageTicketRepository.findById(stageTicketId))
                .map(NewTicket::getAmount)
                .hasValue(100);
        }

        @Test
        void 공연과_티켓_타입이_같으면_기존_StageTicket에_TicketEntryTime이_추가된다() {
            // given
            var command = StageTicketCreateCommand.builder()
                .schoolId(테코대학교.getId())
                .stageId(테코대학교_1일차_공연.getId())
                .ticketExclusive(TicketExclusive.STUDENT)
                .amount(100)
                .entryTime(_6월_15일_17시_0분)
                .build();

            // when
            Long firstStageTicketId = stageTicketCreateService.createStageTicket(command);
            Long secondStageTicketId = stageTicketCreateService.createStageTicket(command);

            // then
            assertThat(firstStageTicketId).isEqualTo(secondStageTicketId);
            assertThat(stageTicketRepository.findById(firstStageTicketId))
                .map(NewTicket::getAmount)
                .hasValue(200);
        }

        @Test
        void 공연이_같아도_티켓_타입이_다르면_새로운_Ticket이_추가된다() {
            // given
            var studentTicketCommand = StageTicketCreateCommand.builder()
                .schoolId(테코대학교.getId())
                .stageId(테코대학교_1일차_공연.getId())
                .ticketExclusive(TicketExclusive.NONE)
                .amount(100)
                .entryTime(_6월_15일_17시_0분)
                .build();
            var visitorTicketCommand = StageTicketCreateCommand.builder()
                .schoolId(테코대학교.getId())
                .stageId(테코대학교_1일차_공연.getId())
                .ticketExclusive(TicketExclusive.STUDENT)
                .amount(50)
                .entryTime(_6월_15일_17시_0분)
                .build();

            //when
            Long studentTicketId = stageTicketCreateService.createStageTicket(studentTicketCommand);
            Long visitorTicketId = stageTicketCreateService.createStageTicket(visitorTicketCommand);

            // then
            assertThat(studentTicketId).isNotEqualTo(visitorTicketId);
            assertThat(stageTicketRepository.findById(studentTicketId))
                .map(NewTicket::getAmount)
                .hasValue(100);
            assertThat(stageTicketRepository.findById(visitorTicketId))
                .map(NewTicket::getAmount)
                .hasValue(50);
        }
    }
}
