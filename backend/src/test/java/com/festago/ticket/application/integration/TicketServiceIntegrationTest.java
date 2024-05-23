package com.festago.ticket.application.integration;

import static com.festago.common.exception.ErrorCode.STAGE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.ticket.application.TicketService;
import com.festago.ticket.domain.TicketAmount;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.dto.TicketCreateRequest;
import com.festago.ticket.dto.TicketCreateResponse;
import com.festago.ticket.repository.TicketAmountRepository;
import com.festago.ticket.repository.TicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    TicketService ticketService;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TicketAmountRepository ticketAmountRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    Clock clock;

    @Test
    void 공연이_없으면_예외() {
        // given
        LocalDateTime entryTime = LocalDateTime.parse("2022-07-26T18:00:00");
        long invalidStageId = 0L;
        int totalAmount = 100;

        TicketCreateRequest request = new TicketCreateRequest(invalidStageId, TicketType.VISITOR,
            totalAmount, entryTime);

        // when && then
        assertThatThrownBy(() -> ticketService.create(request))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(STAGE_NOT_FOUND.getMessage());
    }

    @Test
    void 공연에_티켓_추가() {
        // given
        LocalDateTime stageStartTime = LocalDateTime.parse("2022-07-26T18:00:00");
        doReturn(stageStartTime.minusWeeks(1).toInstant(ZoneOffset.UTC))
            .when(clock).instant();
        School school = schoolRepository.save(SchoolFixture.builder().build());
        Festival festival = festivalRepository.save(FestivalFixture.builder()
            .school(school)
            .startDate(stageStartTime.toLocalDate())
            .endDate(stageStartTime.toLocalDate())
            .build());
        Stage stage = stageRepository.save(StageFixture.builder()
            .festival(festival)
            .startTime(stageStartTime)
            .ticketOpenTime(stageStartTime.minusDays(1))
            .build());
        TicketCreateRequest request = new TicketCreateRequest(stage.getId(), TicketType.VISITOR,
            100, stageStartTime.minusHours(1));

        // when
        TicketCreateResponse response = ticketService.create(request);

        // then
        TicketAmount ticketAmount = ticketAmountRepository.findById(response.id()).get();
        assertThat(ticketAmount.getTotalAmount()).isEqualTo(100);
    }

    @Test
    void 티켓이_있는_공연에_티켓을_추가하면_기존_티켓의_수량이_증가() {
        // given
        LocalDateTime stageStartTime = LocalDateTime.parse("2022-07-26T18:00:00");
        doReturn(stageStartTime.minusWeeks(1).toInstant(ZoneOffset.UTC))
            .when(clock).instant();
        School school = schoolRepository.save(SchoolFixture.builder().build());
        Festival festival = festivalRepository.save(FestivalFixture.builder()
            .school(school)
            .startDate(stageStartTime.toLocalDate())
            .endDate(stageStartTime.toLocalDate())
            .build());
        Stage stage = stageRepository.save(StageFixture.builder()
            .festival(festival)
            .startTime(stageStartTime)
            .ticketOpenTime(stageStartTime.minusDays(1))
            .build());
        TicketCreateRequest request = new TicketCreateRequest(stage.getId(), TicketType.VISITOR,
            100, stageStartTime.minusHours(1));

        ticketService.create(request);

        // when
        TicketCreateResponse response = ticketService.create(request);

        // then
        assertThat(ticketRepository.count()).isEqualTo(1);
        TicketAmount ticketAmount = ticketAmountRepository.findById(response.id()).get();
        assertThat(ticketAmount.getTotalAmount()).isEqualTo(200);
    }
}
