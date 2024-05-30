package com.festago.stage.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.artist.infrastructure.DelimiterArtistsSerializer;
import com.festago.artist.repository.ArtistRepository;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.repository.MemoryStageQueryInfoRepository;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.stage.repository.StageQueryInfoRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.support.fixture.StageQueryInfoFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageQueryInfoServiceTest {

    StageQueryInfoService stageQueryInfoService;

    StageQueryInfoRepository stageQueryInfoRepository;

    StageRepository stageRepository;

    ArtistRepository artistRepository;

    ArtistsSerializer artistsSerializer = new DelimiterArtistsSerializer(",");

    Stage 공연;

    Artist 뉴진스;

    @BeforeEach
    void setUp() {
        stageQueryInfoRepository = new MemoryStageQueryInfoRepository();
        stageRepository = new MemoryStageRepository();
        artistRepository = new MemoryArtistRepository();
        stageQueryInfoService = new StageQueryInfoService(
            stageQueryInfoRepository,
            artistRepository,
            artistsSerializer
        );
        뉴진스 = artistRepository.save(ArtistFixture.builder().name("뉴진스").build());
        공연 = stageRepository.save(StageFixture.builder().build());
        공연.renewArtists(List.of(뉴진스.getId()));
    }

    @Nested
    class initialStageQueryInfo {

        @Test
        void StageQueryInfo가_생성된다() {
            // when
            stageQueryInfoService.initialStageQueryInfo(공연);

            // then
            assertThat(stageQueryInfoRepository.findByStageId(공연.getId())).isPresent();
        }
    }

    @Nested
    class renewalStageQueryInfo {

        @Test
        void Stage_식별자에_대한_StageQueryInfo가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> stageQueryInfoService.renewalStageQueryInfo(공연))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.STAGE_NOT_FOUND.getMessage());
        }

        @Test
        void StageQueryInfo가_새롭게_갱신된다() {
            // given
            stageQueryInfoRepository.save(
                StageQueryInfoFixture.builder().stageId(공연.getId()).artistInfo("oldInfo").build());

            // when
            stageQueryInfoService.renewalStageQueryInfo(공연);

            // then
            StageQueryInfo stageQueryInfo = stageQueryInfoRepository.findByStageId(공연.getId()).get();
            assertThat(stageQueryInfo.getArtistInfo()).isNotEqualTo("oldInfo");
        }
    }

    @Nested
    class deleteStageQueryInfo {

        @Test
        void StageQueryInfo가_삭제된다() {
            // given
            stageQueryInfoRepository.save(StageQueryInfoFixture.builder().stageId(공연.getId()).build());

            // when
            stageQueryInfoService.deleteStageQueryInfo(공연.getId());

            // then
            assertThat(stageQueryInfoRepository.findByStageId(공연.getId())).isEmpty();
        }
    }
}
