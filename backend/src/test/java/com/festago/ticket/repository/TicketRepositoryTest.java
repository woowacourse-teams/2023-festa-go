package com.festago.ticket.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.RepositoryTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.support.fixture.TicketFixture;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
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
        School school = schoolRepository.save(SchoolFixture.builder().build());
        Festival festival = festivalRepository.save(FestivalFixture.builder().school(school).build());
        Stage stage = stageRepository.save(StageFixture.builder().festival(festival).build());
        Stage otherStage = stageRepository.save(StageFixture.builder().festival(festival).build());

        ticketRepository.save(TicketFixture.builder().stage(stage).ticketType(TicketType.STUDENT).build());
        ticketRepository.save(TicketFixture.builder().stage(stage).ticketType(TicketType.VISITOR).build());
        ticketRepository.save(TicketFixture.builder().stage(otherStage).build());

        // when
        List<Ticket> actual = ticketRepository.findAllByStageIdWithFetch(stage.getId());

        // then
        assertThat(actual).hasSize(2);
    }
}
