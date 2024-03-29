package com.festago.mock.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.mock.MockArtist;
import com.festago.mock.MockDataService;
import com.festago.mock.config.MockDataConfig;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageQueryInfoRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.SchoolFixture;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import(MockDataConfig.class)
class MockDataServiceTest extends ApplicationIntegrationTest {

    private static final int INCLUDE_FIRST_DATE = 1;
    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    StageArtistRepository stageArtistRepository;

    @Autowired
    StageQueryInfoRepository stageQueryInfoRepository;

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

    @Autowired
    MockDataService mockDataService;

    @Nested
    class 목_데이터_초기화는 {

        @Test
        void 만약_하나의_학교라도_존재하면_초기화_된_상태로_판단한다() {
            // given
            schoolRepository.save(SchoolFixture.builder().build());

            // when
            mockDataService.initialize();
            List<School> allSchool = schoolRepository.findAll();

            // then
            assertThat(allSchool).hasSize(1);
        }

        @Test
        void 학교가_없다면_ANY_를_제외한_지역_곱하기_3개만큼의_학교와_MOCK_ARTIST_만큼의_아티스트를_생성한다() {
            // given
            mockDataService.initialize();
            int expectGeneratedSchoolSize = (SchoolRegion.values().length - 1) * 3;
            int expectArtistSize = MockArtist.values().length;

            // when
            List<School> allSchool = schoolRepository.findAll();
            List<Artist> allArtist = artistRepository.findAll();

            // then
            assertSoftly(softly -> {
                softly.assertThat(allSchool).hasSize(expectGeneratedSchoolSize);
                softly.assertThat(allArtist).hasSize(expectArtistSize);
            });
        }
    }

    @Nested
    class 목_축제_생성_요청은 {

        @Test
        @Transactional
        void 존재하는_학교수_만큼의_축제를_만들어_낸다() {
            // given
            mockDataService.initialize();
            List<School> allSchool = schoolRepository.findAll();
            List<Festival> beforeFestivals = festivalRepository.findAll();

            // when
            mockDataService.makeMockFestivals(7);
            List<Festival> afterFestivals = festivalRepository.findAll();
            List<School> festivalSchools = afterFestivals.stream()
                .map(festival -> festival.getSchool())
                .toList();

            // then
            assertSoftly(softly -> {
                softly.assertThat(beforeFestivals).hasSize(0);
                softly.assertThat(afterFestivals).hasSize(allSchool.size());
                softly.assertThat(festivalSchools).containsAll(allSchool);
            });
        }

        @Test
        void 쿼리_최적화_정보들을_생성한다() {
            // given
            mockDataService.initialize();
            mockDataService.makeMockFestivals(7);

            // when
            List<StageQueryInfo> stageQueryInfos = stageQueryInfoRepository.findAll();
            List<FestivalQueryInfo> festivalQueryInfos = festivalInfoRepository.findAll();

            // then
            assertSoftly(softly -> {
                assertThat(stageQueryInfos).isNotEmpty();
                assertThat(festivalQueryInfos).isNotEmpty();
            });
        }

        @Test
        void 생성된_모든_축제는_기간은_전달_받은_기간_이내_이다() {
            // given
            mockDataService.initialize();
            mockDataService.makeMockFestivals(1);

            // when
            List<Festival> allFestival = festivalRepository.findAll();

            // then
            assertThat(allFestival).allMatch(
                festival -> festival.getStartDate().until(festival.getStartDate(), ChronoUnit.DAYS) == 0);
        }

        @Test
        @Transactional
        void 무대는_생성된_축제_기간동안_전부_존재한다() {
            // given
            LocalDate now = LocalDate.now();

            mockDataService.initialize();
            mockDataService.makeMockFestivals(7);
            List<Festival> allFestival = festivalRepository.findAll();

            // when
            List<Stage> allStage = stageRepository.findAll();
            Map<Festival, List<Stage>> stageByFestival = allStage.stream()
                .collect(Collectors.groupingBy(Stage::getFestival));

            // then
            assertThat(stageByFestival.entrySet()).allMatch(festivalListEntry -> {
                Festival festival = festivalListEntry.getKey();
                long festivalDuration =
                    festival.getStartDate().until(festival.getEndDate(), ChronoUnit.DAYS) + INCLUDE_FIRST_DATE;
                return festivalListEntry.getValue().size() == festivalDuration;
            });
        }

        @Test
        void 같은_축제_속_무대_아티스트_들은_겹치지_않는다() {
            // given
            mockDataService.initialize();
            mockDataService.makeMockFestivals(7);

            List<StageArtist> stageArtists = stageArtistRepository.findAll();
            List<Stage> allStage = stageRepository.findAll();

            Map<Long, List<StageArtist>> stageArtistByStageId = stageArtists.stream()
                .collect(Collectors.groupingBy(StageArtist::getStageId));
            Map<Festival, List<Stage>> stageByFestival = allStage.stream()
                .collect(Collectors.groupingBy(Stage::getFestival));

            // when
            Map<Festival, List<StageArtist>> stageArtistsByFestival = new HashMap<>();

            stageByFestival.forEach((festival, stages) -> {
                List<StageArtist> artistsForFestival = stages.stream()
                    .map(stage -> stageArtistByStageId.getOrDefault(stage.getId(), Collections.emptyList()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

                stageArtistsByFestival.put(festival, artistsForFestival);
            });

            // then
            assertThat(stageArtistsByFestival.keySet())
                .allMatch(festival -> {
                    List<StageArtist> stageArtistsValue = stageArtistsByFestival.get(festival);
                    long uniqueStageArtists = stageArtistsValue.stream()
                        .map(stageArtist -> stageArtist.getArtistId())
                        .distinct()
                        .count();
                    return stageArtistsValue.size() == uniqueStageArtists;
                });
        }
    }
}
