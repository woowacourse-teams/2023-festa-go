package com.festago.admin.application.integration;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.admin.application.AdminStageV1QueryService;
import com.festago.admin.dto.stage.AdminStageArtistV1Response;
import com.festago.admin.dto.stage.AdminStageV1Response;
import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.dto.command.ArtistCreateCommand;
import com.festago.festival.application.command.FestivalCreateService;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.stage.application.command.StageCreateService;
import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.support.ApplicationIntegrationTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminStageV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    AdminStageV1QueryService adminStageV1QueryService;

    @Autowired
    FestivalCreateService festivalCreateService;

    @Autowired
    StageCreateService stageCreateService;

    @Autowired
    SchoolCommandService schoolCommandService;

    @Autowired
    ArtistCommandService artistCommandService;

    LocalDate _2077년_6월_15일 = LocalDate.parse("2077-06-15");
    LocalDate _2077년_6월_16일 = LocalDate.parse("2077-06-16");
    LocalDate _2077년_6월_17일 = LocalDate.parse("2077-07-17");

    @Test
    void 존재하지_않는_축제의_식별자로_조회하면_빈_리스트가_반환된다() {
        // when
        var actual = adminStageV1QueryService.findAllByFestivalId(4885L);

        // then
        assertThat(actual).isEmpty();
    }

    @Nested
    class 축제에_공연이_없으면 {

        Long 축제_식별자;

        @BeforeEach
        void setUp() {
            Long 대학교_식별자 = schoolCommandService.createSchool(new SchoolCreateCommand(
                "테코대학교",
                "teco.ac.kr",
                SchoolRegion.서울,
                "https://image.com/logo.png",
                "https://image.com/backgroundImage.png"
            ));
            축제_식별자 = festivalCreateService.createFestival(new FestivalCreateCommand(
                "테코대학교 축제",
                _2077년_6월_15일,
                _2077년_6월_15일,
                "https://image.com/posterImage.png",
                대학교_식별자
            ));
        }

        @Test
        void 빈_리스트가_반환된다() {
            // when
            var actual = adminStageV1QueryService.findAllByFestivalId(축제_식별자);

            // then
            assertThat(actual).isEmpty();
        }
    }

    /**
     * 6월 15일 ~ 6월 17일까지 진행되는 축제<p> 6월 15일 공연, 6월 16일 공연이 있다.<p>
     * <p>
     * 6월 15일 공연에는 아티스트A, 아티스트B가 참여한다.<p> 6월 16일 공연에는 아티스트C가 참여한다.
     */
    @Nested
    class 특정_축제의_공연_목록과_참여하는_아티스트를_조회할_수_있어야_한다 {

        Long 아티스트A_식별자;
        Long 아티스트B_식별자;
        Long 아티스트C_식별자;
        Long 축제_식별자;
        Long _6월_15일_공연_식별자;
        Long _6월_16일_공연_식별자;

        @BeforeEach
        void setUp() {
            아티스트A_식별자 = createArtist("아티스트A");
            아티스트B_식별자 = createArtist("아티스트B");
            아티스트C_식별자 = createArtist("아티스트C");
            Long 대학교_식별자 = schoolCommandService.createSchool(new SchoolCreateCommand(
                "테코대학교",
                "teco.ac.kr",
                SchoolRegion.서울,
                "https://image.com/logo.png",
                "https://image.com/backgroundImage.png"
            ));
            축제_식별자 = festivalCreateService.createFestival(new FestivalCreateCommand(
                "테코대학교 축제",
                _2077년_6월_15일,
                _2077년_6월_17일,
                "https://image.com/posterImage.png",
                대학교_식별자
            ));
            _6월_15일_공연_식별자 = createStage(축제_식별자, _2077년_6월_15일, List.of(아티스트A_식별자, 아티스트B_식별자));
            _6월_16일_공연_식별자 = createStage(축제_식별자, _2077년_6월_16일, List.of(아티스트C_식별자));
        }

        private Long createArtist(String artistName) {
            return artistCommandService.save(new ArtistCreateCommand(
                artistName,
                "https://image.com/profileImage.png",
                "https://image.com/backgroundImage.png"
            ));
        }

        private Long createStage(Long festivalId, LocalDate localDate, List<Long> artistIds) {
            return stageCreateService.createStage(new StageCreateCommand(
                festivalId,
                localDate.atTime(18, 0),
                localDate.minusWeeks(1).atStartOfDay(),
                artistIds
            ));
        }

        @Test
        void 공연의_시작_순서대로_정렬된다() {
            // when
            var actual = adminStageV1QueryService.findAllByFestivalId(축제_식별자);

            // then
            assertThat(actual)
                .map(AdminStageV1Response::id)
                .containsExactly(_6월_15일_공연_식별자, _6월_16일_공연_식별자);
        }

        @Test
        void 해당_일자의_공연에_참여하는_아티스트_목록을_조회할_수_있다() {
            // when
            var stageIdToArtists = adminStageV1QueryService.findAllByFestivalId(축제_식별자).stream()
                .collect(toMap(AdminStageV1Response::id, AdminStageV1Response::artists));

            // then
            assertSoftly(softly -> {
                softly.assertThat(stageIdToArtists.get(_6월_15일_공연_식별자))
                    .map(AdminStageArtistV1Response::id)
                    .containsExactlyInAnyOrder(아티스트A_식별자, 아티스트B_식별자);

                softly.assertThat(stageIdToArtists.get(_6월_16일_공연_식별자))
                    .map(AdminStageArtistV1Response::id)
                    .containsExactlyInAnyOrder(아티스트C_식별자);
            });
        }
    }
}
