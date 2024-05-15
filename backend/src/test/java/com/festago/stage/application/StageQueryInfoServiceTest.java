package com.festago.stage.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.artist.infrastructure.DelimiterArtistsSerializer;
import com.festago.artist.repository.ArtistRepository;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.repository.MemoryStageArtistRepository;
import com.festago.stage.repository.MemoryStageQueryInfoRepository;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageQueryInfoRepository;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.StageArtistFixture;
import com.festago.support.fixture.StageQueryInfoFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageQueryInfoServiceTest {

    private final Long stageId = 1L;

    StageQueryInfoService stageQueryInfoService;

    StageQueryInfoRepository stageQueryInfoRepository;

    StageArtistRepository stageArtistRepository;

    ArtistRepository artistRepository;

    ArtistsSerializer artistsSerializer = new DelimiterArtistsSerializer(",");

    Artist 뉴진스;

    @BeforeEach
    void setUp() {
        stageQueryInfoRepository = new MemoryStageQueryInfoRepository();
        stageArtistRepository = new MemoryStageArtistRepository();
        artistRepository = new MemoryArtistRepository();
        stageQueryInfoService = new StageQueryInfoService(
            stageQueryInfoRepository,
            stageArtistRepository,
            artistRepository,
            artistsSerializer
        );
        뉴진스 = artistRepository.save(ArtistFixture.builder().name("뉴진스").build());
    }

    @Nested
    class initialStageQueryInfo {

        @Test
        void Artist가_존재하지_않으면_예외() {
            // given
            stageArtistRepository.save(StageArtistFixture.builder(stageId, 4885L).build());

            // when
            assertThatThrownBy(() -> stageQueryInfoService.initialStageQueryInfo(stageId))
                .isInstanceOf(InternalServerException.class)
                .hasMessage(ErrorCode.ARTIST_NOT_FOUND.getMessage());
        }

        @Test
        void StageQueryInfo가_생성된다() {
            // given
            stageArtistRepository.save(StageArtistFixture.builder(stageId, 뉴진스.getId()).build());

            // when
            stageQueryInfoService.initialStageQueryInfo(stageId);

            // then
            assertThat(stageQueryInfoRepository.findByStageId(stageId)).isPresent();
        }
    }

    @Nested
    class renewalStageQueryInfo {

        @Test
        void Stage_식별자에_대한_StageQueryInfo가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> stageQueryInfoService.renewalStageQueryInfo(stageId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.STAGE_NOT_FOUND.getMessage());
        }

        @Test
        void StageQueryInfo가_새롭게_갱신된다() {
            // given
            stageQueryInfoRepository.save(
                StageQueryInfoFixture.builder().stageId(stageId).artistInfo("oldInfo").build());
            stageArtistRepository.save(StageArtistFixture.builder(stageId, 뉴진스.getId()).build());

            // when
            stageQueryInfoService.renewalStageQueryInfo(stageId);

            // then
            StageQueryInfo stageQueryInfo = stageQueryInfoRepository.findByStageId(stageId).get();
            assertThat(stageQueryInfo.getArtistInfo()).isNotEqualTo("oldInfo");
        }
    }

    @Nested
    class deleteStageQueryInfo {

        @Test
        void StageQueryInfo가_삭제된다() {
            // given
            stageQueryInfoRepository.save(StageQueryInfoFixture.builder().stageId(stageId).build());

            // when
            stageQueryInfoService.deleteStageQueryInfo(stageId);

            // then
            assertThat(stageQueryInfoRepository.findByStageId(stageId)).isEmpty();
        }
    }
}
