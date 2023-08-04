package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.application.FestivalService;
import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
import com.festago.domain.TicketType;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalDetailResponse;
import com.festago.dto.FestivalDetailStageResponse;
import com.festago.dto.FestivalDetailTicketResponse;
import com.festago.dto.FestivalResponse;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalService festivalService;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void 축제를_생성한다() {
        // given
        LocalDate today = LocalDate.now();
        FestivalCreateRequest request = new FestivalCreateRequest("테코 대학교 축제", today, today.plusDays(1),
            "thumbnail.png");

        // when
        FestivalResponse festivalResponse = festivalService.create(request);

        // then
        assertThat(festivalResponse).isNotNull();
    }

    @Test
    void 축제_상세_정보를_조회한다() {
        // given
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());
        Ticket ticket1 = ticketRepository.save(
            TicketFixture.ticket().stage(stage).ticketType(TicketType.VISITOR).build());
        ticket1.addTicketEntryTime(LocalDateTime.now().minusMinutes(10), 100);
        Ticket ticket2 = ticketRepository.save(
            TicketFixture.ticket().stage(stage).ticketType(TicketType.STUDENT).build());
        ticket2.addTicketEntryTime(LocalDateTime.now().minusMinutes(10), 200);

        entityManager.flush();
        entityManager.clear();

        // when
        FestivalDetailResponse response = festivalService.findDetail(festival.getId());

        // then
        SoftAssertions.assertSoftly(softly -> {
            List<FestivalDetailStageResponse> stages = response.stages();
            softly.assertThat(response.id()).isEqualTo(festival.getId());
            softly.assertThat(stages.stream().map(FestivalDetailStageResponse::id).toList())
                .containsExactly(stage.getId());
            softly.assertThat(stages.get(0).tickets().stream().map(FestivalDetailTicketResponse::id).toList())
                .containsExactly(ticket1.getId(), ticket2.getId());
        });
    }
}
