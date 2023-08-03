package com.festago.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.dto.StageCreateRequest;
import com.festago.dto.StageResponse;
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
