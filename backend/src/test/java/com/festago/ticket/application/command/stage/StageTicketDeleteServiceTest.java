package com.festago.ticket.application.command.stage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.spy;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.verify;

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
import com.festago.ticket.domain.StageTicket;
import com.festago.ticket.dto.command.StageTicketDeleteCommand;
import com.festago.ticket.dto.event.TicketDeletedEvent;
import com.festago.ticket.repository.MemoryStageTicketRepository;
import com.festago.ticket.repository.StageTicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageTicketDeleteServiceTest {

    StageTicketDeleteService stageTicketDeleteService;

    StageTicketRepository stageTicketRepository;

    ApplicationEventPublisher eventPublisher;

    Clock clock;

    Long schoolId = 1L;

    @BeforeEach
    void setUp() {
        stageTicketRepository = new MemoryStageTicketRepository();
        eventPublisher = mock(ApplicationEventPublisher.class);
        clock = spy(Clock.systemDefaultZone());
        stageTicketDeleteService = new StageTicketDeleteService(
            stageTicketRepository,
            eventPublisher,
            clock
        );
    }

    @Nested
    class deleteStageTicket {

        @Test
        void 티켓의_식별자에_해당하는_티켓이_없으면_예외() {
            // given
            var command = StageTicketDeleteCommand.builder()
                .stageTicketId(4885L)
                .build();

            // when & then
            assertThatThrownBy(() -> stageTicketDeleteService.deleteStageTicket(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.TICKET_NOT_FOUND.getMessage());
        }

        @Test
        void 입장_시간을_삭제_후_남은_입장_시간이_있으면_티켓은_삭제되지_않는다() {
            // given
            StageTicket stageTicket = stageTicketRepository.save(createStageTicket());
            LocalDateTime now = stageTicket.getStage().getTicketOpenTime().minusHours(1);
            LocalDateTime entryTime = stageTicket.getStage().getStartTime().minusHours(1L);
            stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100);
            stageTicket.addTicketEntryTime(schoolId, now, entryTime.plusSeconds(1), 100);
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
            var command = StageTicketDeleteCommand.builder()
                .schoolId(schoolId)
                .stageTicketId(stageTicket.getId())
                .entryTime(entryTime)
                .build();

            // when
            stageTicketDeleteService.deleteStageTicket(command);

            // then
            assertThat(stageTicketRepository.findById(stageTicket.getId())).isPresent();
        }

        @Test
        void 입장_시간을_삭제_후_남은_입장_시간이_없으면_티켓이_삭제된다() {
            // given
            StageTicket stageTicket = stageTicketRepository.save(createStageTicket());
            LocalDateTime now = stageTicket.getStage().getTicketOpenTime().minusHours(1);
            LocalDateTime entryTime = stageTicket.getStage().getStartTime().minusHours(1L);
            stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100);
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
            var command = StageTicketDeleteCommand.builder()
                .schoolId(schoolId)
                .stageTicketId(stageTicket.getId())
                .entryTime(entryTime)
                .build();

            // when
            stageTicketDeleteService.deleteStageTicket(command);

            // then
            assertThat(stageTicketRepository.findById(stageTicket.getId())).isEmpty();
        }

        @Test
        void 삭제된_입장_시간이_있으면_티켓_삭제_이벤트가_발행된다() {
            // given
            StageTicket stageTicket = stageTicketRepository.save(createStageTicket());
            LocalDateTime now = stageTicket.getStage().getTicketOpenTime().minusHours(1);
            LocalDateTime entryTime = stageTicket.getStage().getStartTime().minusHours(1L);
            stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100);
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
            var command = StageTicketDeleteCommand.builder()
                .schoolId(schoolId)
                .stageTicketId(stageTicket.getId())
                .entryTime(entryTime)
                .build();

            // when
            stageTicketDeleteService.deleteStageTicket(command);

            // then
            verify(eventPublisher, times(1)).publishEvent(any(TicketDeletedEvent.class));
        }

        @Test
        void 삭제된_입장_시간이_없으면_티켓_삭제_이벤트가_발행되지_않는다() {
            // given
            StageTicket stageTicket = stageTicketRepository.save(createStageTicket());
            LocalDateTime now = stageTicket.getStage().getTicketOpenTime().minusHours(1);
            LocalDateTime entryTime = stageTicket.getStage().getStartTime().minusHours(1L);
            stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100);
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
            var command = StageTicketDeleteCommand.builder()
                .schoolId(schoolId)
                .stageTicketId(stageTicket.getId())
                .entryTime(entryTime.plusSeconds(1))
                .build();

            // when
            stageTicketDeleteService.deleteStageTicket(command);

            // then
            verify(eventPublisher, never()).publishEvent(any(TicketDeletedEvent.class));
        }
    }

    private StageTicket createStageTicket() {
        School school = SchoolFixture.builder()
            .id(schoolId)
            .build();
        Festival festival = FestivalFixture.builder()
            .school(school)
            .build();
        Stage stage = StageFixture.builder()
            .festival(festival)
            .build();
        return StageTicketFixture.builder()
            .stage(stage)
            .schoolId(school.getId())
            .build();
    }
}
