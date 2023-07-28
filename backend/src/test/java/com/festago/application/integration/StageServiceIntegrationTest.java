package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.application.StageService;
import com.festago.domain.FestivalRepository;
import com.festago.dto.StageCreateRequest;
import com.festago.exception.NotFoundException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    StageService stageService;

    @Autowired
    FestivalRepository festivalRepository;

    @Test
    void 축제가_없으면_예외() {
        // given
        String startTime = "2023-07-27T18:00:00";
        String lineUp = "글렌, 애쉬, 오리, 푸우";
        String ticketOpenTime = "2023-07-26T18:00:00";
        long invalidFestivalId = 1L;

        StageCreateRequest request = new StageCreateRequest(LocalDateTime.parse(startTime), lineUp,
            LocalDateTime.parse(ticketOpenTime),
            invalidFestivalId);

        // when && then
        assertThatThrownBy(() -> stageService.create(request))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("존재하지 않는 축제입니다.");
    }
}
