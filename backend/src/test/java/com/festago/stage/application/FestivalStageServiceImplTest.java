package com.festago.stage.application;

import static com.festago.common.exception.ErrorCode.FESTIVAL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.DetailFestivalResponse;
import com.festago.festival.dto.DetailFestivalResponse.DetailStageResponse;
import com.festago.festival.repository.MemoryFestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalStageServiceImplTest {

    MemoryStageRepository stageRepository = new MemoryStageRepository();

    MemoryFestivalRepository festivalRepository = new MemoryFestivalRepository();

    FestivalStageServiceImpl festivalStageService = new FestivalStageServiceImpl(
        stageRepository,
        festivalRepository
    );

    @BeforeEach
    void setUp() {
        stageRepository.clear();
        festivalRepository.clear();
    }

    @Nested
    class 축제_무대_상세_조회 {

        @Test
        void 존재하지_않는_축제에_대한_상세_무대_조희를_하면_예외() {
            // given
            Long invalidFestivalId = 1L;

            // when & then
            assertThatThrownBy(() -> festivalStageService.findDetail(invalidFestivalId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(FESTIVAL_NOT_FOUND.getMessage());
        }

        @Test
        void 무대_시작시간순으로_정렬() {
            // given
            LocalDateTime now = LocalDateTime.parse("2023-01-10T16:00:00");
            LocalDateTime ticketOpenTime = now.minusDays(1);

            Festival festival = FestivalFixture.festival()
                .startDate(now.minusDays(1).toLocalDate())
                .endDate(now.plusDays(1).toLocalDate())
                .build();
            festivalRepository.save(festival);

            Stage firstStage = StageFixture.stage()
                .ticketOpenTime(ticketOpenTime)
                .startTime(now)
                .festival(festival)
                .build();
            stageRepository.save(firstStage);

            Stage secondStage = StageFixture.stage()
                .ticketOpenTime(ticketOpenTime)
                .startTime(now.plusDays(1))
                .festival(festival)
                .build();
            stageRepository.save(secondStage);

            // when
            DetailFestivalResponse response = festivalStageService.findDetail(festival.getId());

            // then
            assertThat(response.stages())
                .map(DetailStageResponse::id)
                .containsExactly(
                    firstStage.getId(),
                    secondStage.getId()
                );
        }
    }
}
