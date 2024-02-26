package com.festago.stage.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.admin.dto.ArtistCreateRequest;
import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.domain.Artist;
import com.festago.festival.application.command.FestivalCreateService;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.stage.dto.command.StageUpdateCommand;
import com.festago.stage.repository.StageQueryInfoRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.TimeInstantProvider;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class StageCommandServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    StageCreateService stageCreateService;

    @Autowired
    StageUpdateService stageUpdateService;

    @Autowired
    StageDeleteService stageDeleteService;

    @Autowired
    FestivalCreateService festivalCreateService;

    @Autowired
    SchoolCommandService schoolCommandService;

    @Autowired
    ArtistCommandService artistCommandService;

    @Autowired
    StageQueryInfoRepository stageQueryInfoRepository;

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Clock clock;

    LocalDateTime now = LocalDateTime.parse("2077-06-29T18:00:00");
    LocalDate festivalStartDate = LocalDate.parse("2077-06-30");
    LocalDate festivalEndDate = LocalDate.parse("2077-07-02");
    Long 테코대학교_식별자;
    Long 테코대학교_축제_식별자;
    Long 에픽하이_식별자;
    Long 소녀시대_식별자;
    Long 뉴진스_식별자;

    @BeforeEach
    void setUp() {
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(now));

        테코대학교_식별자 = schoolCommandService.createSchool(new SchoolCreateCommand(
            "테코대학교",
            "teco.ac.kr",
            SchoolRegion.서울,
            "https://image.com/logo.png",
            "https://image.com/backgroundImage.png"
        ));
        테코대학교_축제_식별자 = festivalCreateService.createFestival(new FestivalCreateCommand(
            "테코대학교 축제",
            festivalStartDate,
            festivalEndDate,
            "https://image.com/posterImage.png",
            테코대학교_식별자
        ));
        에픽하이_식별자 = artistCommandService.save(
            new ArtistCreateRequest("에픽하이", "https://image.com/profileImage.png"));
        소녀시대_식별자 = artistCommandService.save(
            new ArtistCreateRequest("소녀시대", "https://image.com/profileImage.png"));
        뉴진스_식별자 = artistCommandService.save(
            new ArtistCreateRequest("뉴진스", "https://image.com/profileImage.png")
        );
    }

    @Nested
    class createStage {

        @Test
        void 공연을_생성하면_StageQueryInfo가_저장된다() {
            // given
            var command = new StageCreateCommand(
                테코대학교_축제_식별자,
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이_식별자, 소녀시대_식별자, 뉴진스_식별자)
            );

            // when
            Long stageId = stageCreateService.createStage(command);

            // then
            assertThat(stageQueryInfoRepository.findByStageId(stageId)).isPresent();
        }

        @Test
        void 공연을_생성하면_FestivalQueryInfo가_갱신된다() {
            // given
            var command = new StageCreateCommand(
                테코대학교_축제_식별자,
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이_식별자, 소녀시대_식별자, 뉴진스_식별자)
            );

            // when
            FestivalQueryInfo previosFestivalQueryInfo = festivalInfoRepository.findByFestivalId(테코대학교_축제_식별자).get();
            Long stageId = stageCreateService.createStage(command);

            // then
            FestivalQueryInfo festivalQueryInfo = festivalInfoRepository.findByFestivalId(테코대학교_축제_식별자).get();
            StageQueryInfo stageQueryInfo = stageQueryInfoRepository.findByStageId(stageId).get();

            assertThat(festivalQueryInfo.getArtistInfo()).isNotEqualTo(previosFestivalQueryInfo.getArtistInfo());
            assertThat(festivalQueryInfo.getArtistInfo()).isEqualTo(stageQueryInfo.getArtistInfo());
        }

        @Test
        void 공연이_여러_개_추가되면_FestivalQueryInfo에_추가된_공연의_ArtistInfo가_갱신된다() throws Exception {
            // given
            var firstCommand = new StageCreateCommand(
                테코대학교_축제_식별자,
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이_식별자)
            );
            var secondCommand = new StageCreateCommand(
                테코대학교_축제_식별자,
                festivalStartDate.plusDays(1).atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(소녀시대_식별자)
            );

            // when
            stageCreateService.createStage(firstCommand);
            stageCreateService.createStage(secondCommand);

            // then
            FestivalQueryInfo festivalQueryInfo = festivalInfoRepository.findByFestivalId(테코대학교_축제_식별자).get();
            List<Artist> actual = Arrays.asList(
                objectMapper.readValue(festivalQueryInfo.getArtistInfo(), Artist[].class)
            );
            assertThat(actual)
                .map(Artist::getId)
                .containsExactlyInAnyOrder(에픽하이_식별자, 소녀시대_식별자);
        }

        @Test
        void 공연이_여러_개_추가될때_공연에_중복된_아티스트가_있어도_FestivalQueryInfo에는_중복이_없다() throws Exception {
            // given
            var firstCommand = new StageCreateCommand(
                테코대학교_축제_식별자,
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이_식별자, 소녀시대_식별자, 뉴진스_식별자)
            );
            var secondCommand = new StageCreateCommand(
                테코대학교_축제_식별자,
                festivalStartDate.plusDays(1).atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이_식별자, 소녀시대_식별자, 뉴진스_식별자)
            );

            // when
            stageCreateService.createStage(firstCommand);
            stageCreateService.createStage(secondCommand);

            // then
            FestivalQueryInfo festivalQueryInfo = festivalInfoRepository.findByFestivalId(테코대학교_축제_식별자).get();
            List<Artist> actual = Arrays.asList(
                objectMapper.readValue(festivalQueryInfo.getArtistInfo(), Artist[].class)
            );
            assertThat(actual)
                .map(Artist::getId)
                .containsExactlyInAnyOrder(에픽하이_식별자, 소녀시대_식별자, 뉴진스_식별자);
        }
    }

    @Nested
    class updateStage {

        Long 테코대학교_축제_공연_식별자;

        @BeforeEach
        void setUp() {
            테코대학교_축제_공연_식별자 = stageCreateService.createStage(new StageCreateCommand(
                테코대학교_축제_식별자,
                festivalStartDate.atTime(18, 0),
                now.minusWeeks(1),
                List.of(에픽하이_식별자, 소녀시대_식별자, 뉴진스_식별자)
            ));
        }

        @Test
        void 공연을_수정하면_StageQueryInfo가_갱신된다() throws Exception {
            // given
            var command = new StageUpdateCommand(
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이_식별자, 소녀시대_식별자)
            );

            // when
            stageUpdateService.updateStage(테코대학교_축제_공연_식별자, command);

            // then
            StageQueryInfo stageQueryInfo = stageQueryInfoRepository.findByStageId(테코대학교_축제_공연_식별자).get();
            List<Artist> actual = Arrays.asList(
                objectMapper.readValue(stageQueryInfo.getArtistInfo(), Artist[].class)
            );
            assertThat(actual)
                .map(Artist::getId)
                .containsExactlyInAnyOrder(에픽하이_식별자, 소녀시대_식별자);
        }

        @Test
        void 공연을_수정하면_FestivalQueryInfo가_갱신된다() throws Exception {
            // given
            var command = new StageUpdateCommand(
                festivalStartDate.atTime(18, 0),
                festivalStartDate.minusWeeks(1).atStartOfDay(),
                List.of(에픽하이_식별자, 소녀시대_식별자)
            );

            // when
            stageUpdateService.updateStage(테코대학교_축제_공연_식별자, command);

            FestivalQueryInfo festivalQueryInfo = festivalInfoRepository.findByFestivalId(테코대학교_축제_식별자).get();
            List<Artist> actual = Arrays.asList(
                objectMapper.readValue(festivalQueryInfo.getArtistInfo(), Artist[].class)
            );
            assertThat(actual)
                .map(Artist::getId)
                .containsExactlyInAnyOrder(에픽하이_식별자, 소녀시대_식별자);
        }
    }

    @Nested
    class deleteStage {

        Long 테코대학교_축제_공연_식별자;

        @BeforeEach
        void setUp() {
            테코대학교_축제_공연_식별자 = stageCreateService.createStage(new StageCreateCommand(
                테코대학교_축제_식별자,
                festivalStartDate.atTime(18, 0),
                now.minusWeeks(1),
                List.of(에픽하이_식별자, 소녀시대_식별자, 뉴진스_식별자)
            ));
        }

        @Test
        void 공연을_삭제하면_StageQueryInfo가_삭제된다() {
            // when
            stageDeleteService.deleteStage(테코대학교_축제_공연_식별자);

            // then
            assertThat(stageQueryInfoRepository.findByStageId(테코대학교_축제_공연_식별자)).isEmpty();
        }

        @Test
        void 공연을_삭제하면_FestivalQueryInfo가_갱신된다() {
            // when
            stageDeleteService.deleteStage(테코대학교_축제_공연_식별자);

            // then
            FestivalQueryInfo festivalQueryInfo = festivalInfoRepository.findByFestivalId(테코대학교_축제_식별자).get();
            assertThat(festivalQueryInfo.getArtistInfo()).isEqualTo("[]");
        }
    }
}
