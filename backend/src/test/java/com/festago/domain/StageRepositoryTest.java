package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
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
    TicketAmountRepository ticketAmountRepository;

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
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());

        // when
        List<Stage> actual = stageRepository.findAllDetailByFestivalId(festival.getId());

        // then
        assertThat(actual.size()).isEqualTo(1);
    }
}
