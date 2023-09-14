package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.repository.TicketRepository;
import com.festago.zfestival.domain.Festival;
import com.festago.zfestival.repository.FestivalRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageRepositoryTest {

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void 티켓이_존재하지_않을때도_무대가_조회된다() {
        // given
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        Stage stage1 = stageRepository.save(StageFixture.stage().festival(festival).build());
        Stage stage2 = stageRepository.save(StageFixture.stage().festival(festival).build());

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
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        stageRepository.save(StageFixture.stage().festival(festival).build());

        // when
        List<Stage> actual = stageRepository.findAllDetailByFestivalId(festival.getId());

        // then
        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void 티켓_정보까지_모두_조회된다() {
        // given
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());
        Ticket ticket1 = ticketRepository.save(
            TicketFixture.ticket().ticketType(TicketType.STUDENT).stage(stage).build());
        Ticket ticket2 = ticketRepository.save(
            TicketFixture.ticket().ticketType(TicketType.VISITOR).stage(stage).build());
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
