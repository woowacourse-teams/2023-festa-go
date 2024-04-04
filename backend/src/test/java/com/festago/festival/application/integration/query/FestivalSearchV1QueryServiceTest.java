package com.festago.festival.application.integration.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.application.FestivalSearchV1QueryService;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.dto.FestivalSearchV1Response;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
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
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalSearchV1QueryServiceTest extends ApplicationIntegrationTest {

    @Autowired
    StageArtistRepository stageArtistRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    FestivalSearchV1QueryService festivalSearchV1QueryService;

    Stage 부산_공연;
    Stage 서울_공연;
    Stage 대구_공연;

    @BeforeEach
    void setting() {
        LocalDate nowDate = LocalDate.now();
        LocalDateTime nowDateTime = LocalDateTime.now();

        School 부산_학교 = schoolRepository.save(SchoolFixture.builder()
            .domain("domain1")
            .name("부산 학교")
            .region(SchoolRegion.부산)
            .build());
        School 서울_학교 = schoolRepository.save(SchoolFixture.builder()
            .domain("domain2")
            .name("서울 학교")
            .region(SchoolRegion.서울)
            .build());
        School 대구_학교 = schoolRepository.save(SchoolFixture.builder()
            .domain("domain3")
            .name("대구 학교")
            .region(SchoolRegion.대구)
            .build());

        Festival 부산_축제 = festivalRepository.save(FestivalFixture.builder()
            .name("부산대학교 축제")
            .startDate(nowDate.minusDays(5))
            .endDate(nowDate.minusDays(1))
            .school(부산_학교)
            .build());
        Festival 서울_축제 = festivalRepository.save(FestivalFixture.builder()
            .name("서울대학교 축제")
            .startDate(nowDate.minusDays(1))
            .endDate(nowDate.plusDays(3))
            .school(서울_학교)
            .build());
        Festival 대구_축제 = festivalRepository.save(FestivalFixture.builder()
            .name("대구대학교 축제")
            .startDate(nowDate.plusDays(1))
            .endDate(nowDate.plusDays(5))
            .school(대구_학교)
            .build());

        festivalInfoRepository.save(FestivalQueryInfo.create(부산_축제.getId()));
        festivalInfoRepository.save(FestivalQueryInfo.create(서울_축제.getId()));
        festivalInfoRepository.save(FestivalQueryInfo.create(대구_축제.getId()));

        부산_공연 = stageRepository.save(StageFixture.builder()
            .startTime(nowDateTime.minusDays(5L))
            .ticketOpenTime(nowDateTime.minusDays(6L))
            .festival(부산_축제)
            .build());
        서울_공연 = stageRepository.save(StageFixture.builder()
            .startTime(nowDateTime.minusDays(1L))
            .ticketOpenTime(nowDateTime.minusDays(2L))
            .festival(서울_축제)
            .build());
        대구_공연 = stageRepository.save(StageFixture.builder()
            .startTime(nowDateTime.plusDays(1L))
            .ticketOpenTime(nowDateTime)
            .festival(대구_축제)
            .build());
    }

    @Nested
    class 학교_기반_축제_검색에서 {

        @Test
        void 대_로끝나는_검색은_학교_검색으로_들어간다() {
            // given
            String keyword = "부산대";

            // when
            List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search(keyword);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual).hasSize(1);
                softly.assertThat(actual.get(0).name()).contains(keyword);
            });
        }

        @Test
        void 대학교_로_끝나는_검색은_학교_검색으로_들어간다() {
            // given
            String keyword = "부산대학교";

            // when
            List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search(keyword);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual).hasSize(1);
                softly.assertThat(actual.get(0).name()).contains(keyword);
            });
        }
    }

    @Nested
    class 아티스트_기반_축제_검색에서 {

        @Nested
        class 두_글자_이상_라이크_검색은 {

            @Test
            void 키워드가_두_글자_이상일_때_해당_키워드를_가진_아티스트의_정보를_반환한다() {
                // given
                Artist 오리 = artistRepository.save(ArtistFixture.builder()
                    .name("오리")
                    .build());
                Artist 우푸우 = artistRepository.save(ArtistFixture.builder()
                    .name("우푸우")
                    .build());
                Artist 글렌 = artistRepository.save(ArtistFixture.builder()
                    .name("글렌")
                    .build());

                stageArtistRepository.save(StageArtistFixture.builder(부산_공연.getId(), 오리.getId()).build());
                stageArtistRepository.save(StageArtistFixture.builder(부산_공연.getId(), 우푸우.getId()).build());

                stageArtistRepository.save(StageArtistFixture.builder(서울_공연.getId(), 오리.getId()).build());
                stageArtistRepository.save(StageArtistFixture.builder(서울_공연.getId(), 글렌.getId()).build());

                stageArtistRepository.save(StageArtistFixture.builder(대구_공연.getId(), 우푸우.getId()).build());

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("푸우");

                // then
                assertSoftly(softly -> {
                    softly.assertThat(actual).hasSize(2);
                    softly.assertThat(actual.get(0).name()).isEqualTo("부산대학교 축제");
                    softly.assertThat(actual.get(1).name()).isEqualTo("대구대학교 축제");
                });
            }

            @Test
            void 해당하는_키워드의_아티스트가_없으면_빈_리스트을_반환한다() {
                // given
                Artist 오리 = artistRepository.save(ArtistFixture.builder()
                    .name("오리")
                    .build());
                Artist 우푸우 = artistRepository.save(ArtistFixture.builder()
                    .name("우푸우")
                    .build());
                Artist 글렌 = artistRepository.save(ArtistFixture.builder()
                    .name("글렌")
                    .build());

                stageArtistRepository.save(StageArtistFixture.builder(부산_공연.getId(), 오리.getId()).build());
                stageArtistRepository.save(StageArtistFixture.builder(부산_공연.getId(), 우푸우.getId()).build());

                stageArtistRepository.save(StageArtistFixture.builder(서울_공연.getId(), 오리.getId()).build());
                stageArtistRepository.save(StageArtistFixture.builder(서울_공연.getId(), 글렌.getId()).build());

                stageArtistRepository.save(StageArtistFixture.builder(대구_공연.getId(), 우푸우.getId()).build());

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("렌글");

                // then
                assertThat(actual).isEmpty();
            }

            @Test
            void 아티스트가_공연에_참여하고_있지_않으면_빈_리스트가_반환된다() {
                // given
                artistRepository.save(ArtistFixture.builder()
                    .name("우푸우")
                    .build());

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("우푸");

                // then
                assertThat(actual).isEmpty();
            }

        }

        @Nested
        class 한_글자_동일_검색은 {

            @Test
            void 두_글자_이상_아티스트와_함께_검색되지_않는다() {
                // given
                Artist 푸우 = artistRepository.save(ArtistFixture.builder()
                    .name("푸우")
                    .build());
                Artist 푸 = artistRepository.save(ArtistFixture.builder()
                    .name("푸")
                    .build());

                stageArtistRepository.save(StageArtistFixture.builder(부산_공연.getId(), 푸우.getId()).build());
                stageArtistRepository.save(StageArtistFixture.builder(부산_공연.getId(), 푸.getId()).build());

                stageArtistRepository.save(StageArtistFixture.builder(서울_공연.getId(), 푸.getId()).build());

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("푸");

                // then
                assertSoftly(softly -> {
                    softly.assertThat(actual).hasSize(2);
                    softly.assertThat(actual.get(0).name()).isEqualTo("부산대학교 축제");
                    softly.assertThat(actual.get(1).name()).isEqualTo("서울대학교 축제");
                });
            }

            @Test
            void 해당하는_키워드의_아티스트가_없으면_빈_리스트를_반환한다() {
                // given
                Artist 푸우 = artistRepository.save(ArtistFixture.builder()
                    .name("푸우")
                    .build());
                Artist 푸푸푸푸 = artistRepository.save(ArtistFixture.builder()
                    .name("푸푸푸푸")
                    .build());
                Artist 글렌 = artistRepository.save(ArtistFixture.builder()
                    .name("글렌")
                    .build());

                stageArtistRepository.save(StageArtistFixture.builder(부산_공연.getId(), 푸우.getId()).build());
                stageArtistRepository.save(StageArtistFixture.builder(부산_공연.getId(), 푸푸푸푸.getId()).build());
                stageArtistRepository.save(StageArtistFixture.builder(부산_공연.getId(), 글렌.getId()).build());

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("푸");

                // then
                assertThat(actual).isEmpty();
            }

            @Test
            void 아티스트가_공연에_참여하고_있지_않으면_빈_리스트를_반환한다() {
                // given
                artistRepository.save(ArtistFixture.builder()
                    .name("우푸우")
                    .build());

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("우푸");

                // then
                assertThat(actual).isEmpty();
            }
        }
    }
}
