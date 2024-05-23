package com.festago.stage.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.support.RepositoryTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.support.fixture.TicketFixture;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.repository.TicketRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class StageRepositoryTest {

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void 티켓이_존재하지_않을때도_무대가_조회된다() {
        // given
        School school = schoolRepository.save(SchoolFixture.builder().build());
        Festival festival = festivalRepository.save(FestivalFixture.builder().school(school).build());
        Stage stage1 = stageRepository.save(StageFixture.builder().festival(festival).build());
        Stage stage2 = stageRepository.save(StageFixture.builder().festival(festival).build());

        // when
        List<Stage> actual = stageRepository.findAllDetailByFestivalId(festival.getId());

        // then
        List<Long> stageIds = actual.stream()
            .map(Stage::getId)
            .toList();
        assertThat(stageIds).containsExactlyInAnyOrder(stage1.getId(), stage2.getId());
    }

    @Test
    void 해당_축제의_무대가_모두_조회된다() {
        // given
        School school = schoolRepository.save(SchoolFixture.builder().build());
        Festival festival = festivalRepository.save(FestivalFixture.builder().school(school).build());
        stageRepository.save(StageFixture.builder().festival(festival).build());

        // when
        List<Stage> actual = stageRepository.findAllDetailByFestivalId(festival.getId());

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void 티켓_정보까지_모두_조회된다() {
        // given
        School school = schoolRepository.save(SchoolFixture.builder().build());
        Festival festival = festivalRepository.save(FestivalFixture.builder().school(school).build());
        Stage stage = stageRepository.save(StageFixture.builder().festival(festival).build());
        Ticket ticket1 = ticketRepository.save(
            TicketFixture.builder().ticketType(TicketType.STUDENT).stage(stage).build());
        Ticket ticket2 = ticketRepository.save(
            TicketFixture.builder().ticketType(TicketType.VISITOR).stage(stage).build());
        ticket1.getTicketAmount().addTotalAmount(100);
        ticket2.getTicketAmount().addTotalAmount(200);
        entityManager.flush();
        entityManager.clear();

        // when
        List<Stage> actual = stageRepository.findAllDetailByFestivalId(festival.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.size()).isEqualTo(1);
            Stage actualStage = actual.get(0);
            softly.assertThat(actualStage.getId()).isEqualTo(stage.getId());
            List<Ticket> actualTickets = actualStage.getTickets();
            softly.assertThat(actualTickets.stream()
                    .map(Ticket::getTicketType)
                    .toList())
                .containsExactlyInAnyOrder(TicketType.STUDENT, TicketType.VISITOR);
            softly.assertThat(actualTickets.stream()
                    .map(ticket -> ticket.getTicketAmount().getTotalAmount())
                    .toList())
                .containsExactlyInAnyOrder(100, 200);
        });
    }
}
