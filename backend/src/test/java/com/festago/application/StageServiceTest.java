package com.festago.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.stage.application.StageService;
import com.festago.stage.domain.Stage;
import com.festago.stage.dto.StageCreateRequest;
import com.festago.stage.dto.StageResponse;
import com.festago.stage.repository.StageRepository;
import com.festago.support.FestivalFixture;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class StageServiceTest {

    @Mock
    StageRepository stageRepository;

    @Mock
    FestivalRepository festivalRepository;

    @InjectMocks
    StageService stageService;

    @Test
    void 무대_생성() {
        // given
        Festival festival = FestivalFixture.festival()
            .build();
        StageCreateRequest request = new StageCreateRequest(
            LocalDateTime.now(),
            "애쉬,푸우,오리,글렌",
            LocalDateTime.now().minusDays(1),
            1L
        );
        given(festivalRepository.findById(anyLong()))
            .willReturn(Optional.of(festival));
        given(stageRepository.save(any(Stage.class)))
            .willAnswer(invocation -> invocation.getArgument(0));

        // when
        StageResponse response = stageService.create(request);

        // then
        assertThat(response.startTime()).isEqualTo(request.startTime());
    }
}
