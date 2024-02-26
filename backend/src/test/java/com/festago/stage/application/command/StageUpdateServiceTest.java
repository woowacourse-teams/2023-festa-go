package com.festago.stage.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.dto.command.StageUpdateCommand;
import com.festago.stage.repository.MemoryStageArtistRepository;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageUpdateServiceTest {

    MemoryStageRepository StageRepository = new MemoryStageRepository();
    MemoryArtistRepository artistRepository = new MemoryArtistRepository();
    MemoryStageArtistRepository stageArtistRepository = new MemoryStageArtistRepository();
    StageUpdateService stageUpdateService = new StageUpdateService(
        StageRepository,
        artistRepository,
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
        StageRepository.clear();
        테코대학교_축제 = FestivalFixture.festival()
            .startDate(stageStartTime.toLocalDate())
            .endDate(stageStartTime.toLocalDate().plusDays(2))
            .build();
        테코대학교_축제_공연 = StageRepository.save(
            StageFixture.stage()
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
    class updateStage {

        @Test
        void Stage에_대한_식별자가_없으면_예외() {
            // given
            Long stageId = 4885L;
            var command = new StageUpdateCommand(
                stageStartTime.minusHours(1),
                ticketOpenTime.minusDays(1),
                List.of(에픽하이.getId(), 소녀시대.getId(), 뉴진스.getId())
            );

            // when & then
            assertThatThrownBy(() -> stageUpdateService.updateStage(stageId, command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.STAGE_NOT_FOUND.getMessage());
        }

        @Test
        void 아티스트_식별자_목록에_존재하지_않은_아티스트가_있으면_예외() {
            // given
            var command = new StageUpdateCommand(
                stageStartTime.minusHours(1),
                ticketOpenTime.minusDays(1),
                List.of(에픽하이.getId(), 소녀시대.getId(), 뉴진스.getId(), 4885L)
            );

            // when & then
            assertThatThrownBy(() -> stageUpdateService.updateStage(테코대학교_축제_공연.getId(), command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ARTIST_NOT_FOUND.getMessage());
        }

        @Test
        void 성공하면_수정된_Stage에_반영된다() {
            // given
            var command = new StageUpdateCommand(
                stageStartTime.minusHours(1),
                ticketOpenTime.minusDays(1),
                List.of(에픽하이.getId(), 소녀시대.getId(), 뉴진스.getId())
            );

            // when
            stageUpdateService.updateStage(테코대학교_축제_공연.getId(), command);

            // then
            Stage actual = StageRepository.findById(테코대학교_축제_공연.getId()).get();
            assertThat(actual.getStartTime()).isEqualTo(command.startTime());
            assertThat(actual.getTicketOpenTime()).isEqualTo(command.ticketOpenTime());
        }

        @Test
        void 성공하면_수정된_Stage에_대한_StageArtist가_갱신된다() {
            // given
            var command = new StageUpdateCommand(
                stageStartTime.minusHours(1),
                ticketOpenTime.minusDays(1),
                List.of(에픽하이.getId())
            );

            // when
            stageUpdateService.updateStage(테코대학교_축제_공연.getId(), command);

            // then
            Set<Long> artistIds = stageArtistRepository.findAllArtistIdByStageId(테코대학교_축제_공연.getId());
            assertThat(artistIds)
                .containsExactly(에픽하이.getId());
        }
    }
}
