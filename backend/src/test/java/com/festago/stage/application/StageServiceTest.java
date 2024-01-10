package com.festago.stage.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.MemoryFestivalRepository;
import com.festago.stage.dto.StageCreateRequest;
import com.festago.stage.dto.StageResponse;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.support.FestivalFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageServiceTest {

    MemoryStageRepository stageRepository = new MemoryStageRepository();

    MemoryFestivalRepository festivalRepository = new MemoryFestivalRepository();

    StageService stageService = new StageService(stageRepository, festivalRepository);

    @BeforeEach
    void setUp() {
        stageRepository.clear();
        festivalRepository.clear();
    }

    @Test
    void 무대_생성() {
        // given
        Festival festival = FestivalFixture.festival()
            .build();
        festivalRepository.save(festival);
        StageCreateRequest request = new StageCreateRequest(
            LocalDateTime.now(),
            "애쉬,푸우,오리,글렌",
            LocalDateTime.now().minusDays(1),
            festival.getId()
        );

        // when
        StageResponse response = stageService.create(request);

        // then
        assertThat(response.startTime()).isEqualTo(request.startTime());
    }
}
