package com.festago.stage.application;

import static com.festago.common.exception.ErrorCode.FESTIVAL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.DetailFestivalResponse;
import com.festago.festival.dto.DetailFestivalResponse.DetailStageResponse;
import com.festago.festival.repository.FestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class FestivalStageServiceImplTest {

    @Mock
    private StageRepository stageRepository;

    @Mock
    private FestivalRepository festivalRepository;

    @InjectMocks
    private FestivalStageServiceImpl festivalStageService;

    @Nested
    class 축제_무대_상세_조회 {

        @Test
        void 존재하지_않는_축제에_대한_상세_무대_조희를_하면_예외() {
            // given
            Long festivalId = 1L;
            given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> festivalStageService.findDetail(festivalId)).isInstanceOf(NotFoundException.class)
                .hasMessage(FESTIVAL_NOT_FOUND.getMessage());
        }

        @Test
        void 무대_시작시간순으로_정렬() {
            // given
            Long festivalId = 1L;
            Festival festival = FestivalFixture.festival().id(festivalId).build();
            LocalDateTime now = LocalDateTime.now();
            Stage stage1 = StageFixture.stage().id(1L).startTime(now).festival(festival).build();
            Stage stage2 = StageFixture.stage().id(2L).startTime(now.plusDays(1)).festival(festival).build();

            given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
            given(stageRepository.findAllDetailByFestivalId(festival.getId())).willReturn(List.of(stage2, stage1));

            // when
            DetailFestivalResponse response = festivalStageService.findDetail(festivalId);

            // then
            List<Long> stageIds = response.stages().stream().map(DetailStageResponse::id).toList();
            assertThat(stageIds).containsExactly(1L, 2L);
        }
    }

}
