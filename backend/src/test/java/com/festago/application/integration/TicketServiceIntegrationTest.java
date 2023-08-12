package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

import com.festago.application.TicketService;
import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.TicketAmount;
import com.festago.domain.TicketAmountRepository;
import com.festago.domain.TicketRepository;
import com.festago.domain.TicketType;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketCreateResponse;
import com.festago.exception.NotFoundException;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

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

    @SpyBean
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
            .hasMessage("존재하지 않은 공연입니다.");
    }

    @Test
    void 공연에_티켓_추가() {
        // given
        LocalDateTime stageStartTime = LocalDateTime.parse("2022-07-26T18:00:00");
        doReturn(stageStartTime.minusWeeks(1).toInstant(ZoneOffset.UTC))
            .when(clock)
            .instant();
        Festival festival = festivalRepository.save(FestivalFixture.festival()
            .startDate(stageStartTime.toLocalDate())
            .endDate(stageStartTime.toLocalDate())
            .build());
        Stage stage = stageRepository.save(StageFixture.stage()
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
            .when(clock)
            .instant();
        Festival festival = festivalRepository.save(FestivalFixture.festival()
            .startDate(stageStartTime.toLocalDate())
            .endDate(stageStartTime.toLocalDate())
            .build());
        Stage stage = stageRepository.save(StageFixture.stage()
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
