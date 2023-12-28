package com.festago.festival.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.application.FestivalService;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.DetailFestivalResponse;
import com.festago.festival.dto.DetailStageResponse;
import com.festago.festival.dto.DetailTicketResponse;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.FestivalResponse;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.FestivalFixture;
import com.festago.support.SchoolFixture;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.repository.TicketRepository;
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
    SchoolRepository schoolRepository;


    @Test
    void 축제를_생성한다() {
        // given
        School school = schoolRepository.save(SchoolFixture.school().build());
        LocalDate today = LocalDate.now();
        FestivalCreateRequest request = new FestivalCreateRequest("테코 대학교 축제", today, today.plusDays(1),
            "thumbnail.png", school.getId());

        // when
        FestivalResponse festivalResponse = festivalService.create(request);

        // then
        assertThat(festivalResponse).isNotNull();
    }

    @Test
    void 축제_상세_정보를_조회한다() {
        // given
        School school = schoolRepository.save(SchoolFixture.school().build());
        Festival festival = festivalRepository.save(FestivalFixture.festival().school(school).build());
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());
        Ticket ticket1 = ticketRepository.save(
            TicketFixture.ticket().stage(stage).ticketType(TicketType.VISITOR).build());
        LocalDateTime ticketOpenTime = stage.getTicketOpenTime();
        ticket1.addTicketEntryTime(ticketOpenTime.minusHours(1), LocalDateTime.now().minusMinutes(10), 100);
        Ticket ticket2 = ticketRepository.save(
            TicketFixture.ticket().stage(stage).ticketType(TicketType.STUDENT).build());
        ticket2.addTicketEntryTime(ticketOpenTime.minusHours(1), LocalDateTime.now().minusMinutes(10), 200);

        // when
        DetailFestivalResponse response = festivalService.findDetail(festival.getId());

        // then
        SoftAssertions.assertSoftly(softly -> {
            List<DetailStageResponse> stages = response.stages();
            softly.assertThat(response.id()).isEqualTo(festival.getId());
            softly.assertThat(stages.stream().map(DetailStageResponse::id).toList())
                .containsExactly(stage.getId());
            softly.assertThat(stages.get(0).tickets().stream().map(DetailTicketResponse::id).toList())
                .containsExactly(ticket1.getId(), ticket2.getId());
        });
    }
}
