package com.festago.stage.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.MemoryFestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.repository.MemoryStageArtistRepository;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.support.fixture.FestivalFixture;
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

    MemoryArtistRepository artistRepository = new MemoryArtistRepository();
    MemoryFestivalRepository festivalRepository = new MemoryFestivalRepository();
    MemoryStageRepository stageRepository = new MemoryStageRepository();
    MemoryStageArtistRepository stageArtistRepository = new MemoryStageArtistRepository();
    StageDeleteService stageDeleteService = new StageDeleteService(
        stageRepository,
        stageArtistRepository,
        mock()
    );

    LocalDateTime stageStartTime = LocalDateTime.parse("2077-06-30T18:00:00");
    LocalDateTime ticketOpenTime = stageStartTime.minusWeeks(1);
    Festival 테코대학교_축제;
    Stage 테코대학교_축제_공연;
    Artist 에픽하이;
    Artist 소녀시대;
    Artist 뉴진스;

    @BeforeEach
    void setUp() {
        stageRepository.clear();
        stageArtistRepository.clear();
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
        에픽하이 = artistRepository.save(new Artist("에픽하이", "https://image.com/profileImage.png"));
        소녀시대 = artistRepository.save(new Artist("소녀시대", "https://image.com/profileImage.png"));
        뉴진스 = artistRepository.save(new Artist("뉴진스", "https://image.com/profileImage.png"));
        stageArtistRepository.save(new StageArtist(테코대학교_축제_공연.getId(), 에픽하이.getId()));
        stageArtistRepository.save(new StageArtist(테코대학교_축제_공연.getId(), 소녀시대.getId()));
        stageArtistRepository.save(new StageArtist(테코대학교_축제_공연.getId(), 뉴진스.getId()));
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
