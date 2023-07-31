package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import com.festago.support.TicketFixture;
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
    TicketAmountRepository ticketAmountRepository;

    @Test
    void 공연의_ID로_티켓을_모두_조회() {
        // given
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());
        Stage otherStage = stageRepository.save(StageFixture.stage().festival(festival).build());

        ticketRepository.save(TicketFixture.ticket().stage(stage).ticketType(TicketType.STUDENT).build());
        ticketRepository.save(TicketFixture.ticket().stage(stage).ticketType(TicketType.VISITOR).build());
        ticketRepository.save(TicketFixture.ticket().stage(otherStage).build());

        // when
        List<Ticket> actual = ticketRepository.findAllByStageId(stage.getId());

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void name() {
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());
        Ticket ticket = ticketRepository.save(new Ticket(stage, TicketType.VISITOR));
        TicketAmount ticketAmount = ticket.getTicketAmount()
            .orElseThrow(IllegalArgumentException::new);
        System.out.println(ticketAmount.getId());
    }
}
