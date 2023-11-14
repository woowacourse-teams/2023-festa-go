package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.FestivalFixture;
import com.festago.support.SchoolFixture;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.repository.TicketRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Test
    void 공연의_ID로_티켓을_모두_조회() {
        // given
        School school = schoolRepository.save(SchoolFixture.school().build());
        Festival festival = festivalRepository.save(FestivalFixture.festival().school(school).build());
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());
        Stage otherStage = stageRepository.save(StageFixture.stage().festival(festival).build());

        ticketRepository.save(TicketFixture.ticket().stage(stage).ticketType(TicketType.STUDENT).build());
        ticketRepository.save(TicketFixture.ticket().stage(stage).ticketType(TicketType.VISITOR).build());
        ticketRepository.save(TicketFixture.ticket().stage(otherStage).build());

        // when
        List<Ticket> actual = ticketRepository.findAllByStageIdWithFetch(stage.getId());

        // then
        assertThat(actual).hasSize(2);
    }
}
