package com.festago.stage.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.mock;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.exception.ValidException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.MemoryFestivalRepository;
import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.stage.repository.MemoryStageArtistRepository;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.support.FestivalFixture;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageCreateServiceTest {

    private static final String PROFILE_IMAGE_URL = "https://image.com/profileImage.png";

    MemoryStageRepository stageRepository = new MemoryStageRepository();
    MemoryFestivalRepository festivalRepository = new MemoryFestivalRepository();
    MemoryArtistRepository artistRepository = new MemoryArtistRepository();
    MemoryStageArtistRepository stageArtistRepository = new MemoryStageArtistRepository();
    StageCreateService stageCreateService = new StageCreateService(
        stageRepository,
        festivalRepository,
        artistRepository,
        stageArtistRepository,
        mock()
    );

    LocalDate festivalStartDate = LocalDate.parse("2077-06-30");
    LocalDate festivalEndDate = LocalDate.parse("2077-07-02");
    Festival 테코대학교_축제;
    Artist 에픽하이;
    Artist 소녀시대;
    Artist 뉴진스;

    @BeforeEach
    void setUp() {
        stageRepository.clear();
        festivalRepository.clear();
        artistRepository.clear();
        stageArtistRepository.clear();

        테코대학교_축제 = festivalRepository.save(
            FestivalFixture.festival()
                .name("테코대학교 축제")
                .startDate(festivalStartDate)
                .endDate(festivalEndDate)
                .build()
        );
        에픽하이 = artistRepository.save(new Artist("에픽하이", PROFILE_IMAGE_URL));
        소녀시대 = artistRepository.save(new Artist("소녀시대", PROFILE_IMAGE_URL));
        뉴진스 = artistRepository.save(new Artist("뉴진스", PROFILE_IMAGE_URL));
    }

    @Nested
    class createStage {

        @Test
        void ArtistIds에_중복이_있으면_예외() {
            // given
            var command = new StageCreateCommand(
                테코대학교_축제.getId(),
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이.getId(), 에픽하이.getId())
            );

            // then
            // when & then
            assertThatThrownBy(() -> stageCreateService.createStage(command))
                .isInstanceOf(ValidException.class)
                .hasMessage("artistIds에 중복된 값이 있습니다.");
        }

        @Test
        void ArtistIds의_개수가_10개를_초과하면_예외() {
            // given
            List<Long> artistIds = LongStream.rangeClosed(1, 11)
                .boxed()
                .toList();
            var command = new StageCreateCommand(
                테코대학교_축제.getId(),
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                artistIds
            );

            // when & then
            assertThatThrownBy(() -> stageCreateService.createStage(command))
                .isInstanceOf(ValidException.class)
                .hasMessage("artistIds의 size는 10 이하여야 합니다.");
        }

        @Test
        void ArtistIds의_개수가_10개_이하이면_예외가_발생하지_않는다() {
            // given
            List<Long> artistIds = LongStream.rangeClosed(1, 10)
                .mapToObj(it -> artistRepository.save(new Artist("Artist " + it, PROFILE_IMAGE_URL)))
                .map(Artist::getId)
                .toList();
            var command = new StageCreateCommand(
                테코대학교_축제.getId(),
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                artistIds
            );

            // when
            assertThatNoException().isThrownBy(() -> stageCreateService.createStage(command));
        }

        @Test
        void Festival_식별자에_대한_Festival이_없으면_예외() {
            // given
            var command = new StageCreateCommand(
                4885L,
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이.getId(), 소녀시대.getId(), 뉴진스.getId())
            );

            // when & then
            assertThatThrownBy(() -> stageCreateService.createStage(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.FESTIVAL_NOT_FOUND.getMessage());
        }

        @Test
        void 아티스트_식별자_목록에_존재하지_않은_아티스트가_있으면_예외() {
            // given
            var command = new StageCreateCommand(
                테코대학교_축제.getId(),
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이.getId(), 소녀시대.getId(), 뉴진스.getId(), 4885L)
            );

            // when & then
            assertThatThrownBy(() -> stageCreateService.createStage(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ARTIST_NOT_FOUND.getMessage());
        }

        @Test
        void 성공하면_생성한_Stage에_대한_StageArtist가_저장된다() {
            // given
            var command = new StageCreateCommand(
                테코대학교_축제.getId(),
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이.getId(), 소녀시대.getId(), 뉴진스.getId())
            );

            // when
            Long stageId = stageCreateService.createStage(command);

            // then
            Set<Long> stageArtists = stageArtistRepository.findAllArtistIdByStageId(stageId);
            assertThat(stageArtists)
                .containsExactlyInAnyOrder(에픽하이.getId(), 소녀시대.getId(), 뉴진스.getId());
        }
    }
}
