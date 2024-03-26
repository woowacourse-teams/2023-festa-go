package com.festago.admin.application.integration;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.admin.application.AdminStageV1QueryService;
import com.festago.admin.dto.stage.AdminStageArtistV1Response;
import com.festago.admin.dto.stage.AdminStageV1Response;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageArtistFixture;
import com.festago.support.fixture.StageFixture;
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
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    StageArtistRepository stageArtistRepository;

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
            var 학교 = schoolRepository.save(SchoolFixture.builder().build());
            축제_식별자 = festivalRepository.save(FestivalFixture.builder()
                .startDate(_2077년_6월_15일)
                .endDate(_2077년_6월_15일)
                .school(학교)
                .build()).getId();
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
        Festival 축제;
        Long _6월_15일_공연_식별자;
        Long _6월_16일_공연_식별자;

        @BeforeEach
        void setUp() {
            아티스트A_식별자 = createArtist("아티스트A");
            아티스트B_식별자 = createArtist("아티스트B");
            아티스트C_식별자 = createArtist("아티스트C");
            var 학교 = schoolRepository.save(SchoolFixture.builder().build());
            축제 = festivalRepository.save(FestivalFixture.builder()
                .startDate(_2077년_6월_15일)
                .endDate(_2077년_6월_17일)
                .school(학교)
                .build());
            _6월_15일_공연_식별자 = createStage(축제, _2077년_6월_15일, List.of(아티스트A_식별자, 아티스트B_식별자)).getId();
            _6월_16일_공연_식별자 = createStage(축제, _2077년_6월_16일, List.of(아티스트C_식별자)).getId();
        }

        private Long createArtist(String artistName) {
            return artistRepository.save(ArtistFixture.builder()
                .name(artistName)
                .build()
            ).getId();
        }

        private Stage createStage(Festival festival, LocalDate localDate, List<Long> artistIds) {
            var 공연 = stageRepository.save(StageFixture.builder()
                .festival(festival)
                .startTime(localDate.atTime(18, 0))
                .build()
            );
            for (Long artistId : artistIds) {
                stageArtistRepository.save(StageArtistFixture.builder(공연.getId(), artistId).build());
            }
            return 공연;
        }

        @Test
        void 공연의_시작_순서대로_정렬된다() {
            // when
            var actual = adminStageV1QueryService.findAllByFestivalId(축제.getId());

            // then
            assertThat(actual)
                .map(AdminStageV1Response::id)
                .containsExactly(_6월_15일_공연_식별자, _6월_16일_공연_식별자);
        }

        @Test
        void 해당_일자의_공연에_참여하는_아티스트_목록을_조회할_수_있다() {
            // when
            var stageIdToArtists = adminStageV1QueryService.findAllByFestivalId(축제.getId()).stream()
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
