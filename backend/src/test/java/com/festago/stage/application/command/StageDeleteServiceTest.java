package com.festago.stage.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.festival.repository.MemoryFestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.MemoryStageArtistRepository;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.StageArtistFixture;
import com.festago.support.fixture.StageFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageDeleteServiceTest {

    ArtistRepository artistRepository;
    FestivalRepository festivalRepository;
    StageRepository stageRepository;
    StageArtistRepository stageArtistRepository;
    StageDeleteService stageDeleteService;

    LocalDateTime stageStartTime = LocalDateTime.parse("2077-06-30T18:00:00");
    LocalDateTime ticketOpenTime = stageStartTime.minusWeeks(1);
    Festival 테코대학교_축제;
    Stage 테코대학교_축제_공연;
    Artist 에픽하이;
    Artist 소녀시대;
    Artist 뉴진스;

    @BeforeEach
    void setUp() {
        artistRepository = new MemoryArtistRepository();
        festivalRepository = new MemoryFestivalRepository();
        stageRepository = new MemoryStageRepository();
        stageArtistRepository = new MemoryStageArtistRepository();
        stageDeleteService = new StageDeleteService(stageRepository, stageArtistRepository, mock());

        테코대학교_축제 = festivalRepository.save(
            FestivalFixture.builder()
                .name("테코대학교 축제")
                .startDate(stageStartTime.toLocalDate())
                .endDate(stageStartTime.toLocalDate().plusDays(2))
                .build()
        );
        테코대학교_축제_공연 = stageRepository.save(
            StageFixture.builder()
                .festival(테코대학교_축제)
                .startTime(stageStartTime)
                .ticketOpenTime(ticketOpenTime)
                .build()
        );
        에픽하이 = artistRepository.save(ArtistFixture.builder().name("에픽하이").build());
        소녀시대 = artistRepository.save(ArtistFixture.builder().name("소녀시대").build());
        뉴진스 = artistRepository.save(ArtistFixture.builder().name("뉴진스").build());
        stageArtistRepository.save(StageArtistFixture.builder(테코대학교_축제_공연.getId(), 에픽하이.getId()).build());
        stageArtistRepository.save(StageArtistFixture.builder(테코대학교_축제_공연.getId(), 소녀시대.getId()).build());
        stageArtistRepository.save(StageArtistFixture.builder(테코대학교_축제_공연.getId(), 뉴진스.getId()).build());
    }

    @Nested
    class deleteStage {

        @Test
        void 삭제하려는_공연의_식별자가_존재하지_않아도_예외가_발생하지_않는다() {
            // given
            Long stageId = 4885L;

            // when
            assertThatNoException()
                .isThrownBy(() -> stageDeleteService.deleteStage(stageId));
        }

        @Test
        void 성공하면_저장된_Stage가_삭제된다() {
            // when
            stageDeleteService.deleteStage(테코대학교_축제_공연.getId());

            // then
            assertThat(stageRepository.findById(테코대학교_축제_공연.getId())).isEmpty();
        }

        @Test
        void 성공하면_식별자에_대한_StageArtist가_삭제된다() {
            // when
            stageDeleteService.deleteStage(테코대학교_축제_공연.getId());

            // then
            assertThat(stageArtistRepository.findAllArtistIdByStageId(테코대학교_축제_공연.getId())).isEmpty();
        }
    }
}
