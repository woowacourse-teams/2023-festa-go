package com.festago.artist.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.application.ArtistSearchStageCountV1QueryService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchStageCountV1Response;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistSearchStageCountV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    private ArtistSearchStageCountV1QueryService artistSearchStageCountV1QueryService;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private StageArtistRepository stageArtistRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Nested
    class 검색 {

        Artist 아이유;
        Artist 아이브;
        Artist 아이들;
        Stage 어제_공연;
        Stage 오늘_공연;
        Stage 내일_공연;
        LocalDate today = LocalDate.now();
        School school;

        @BeforeEach
        void setUp() {
            아이유 = artistRepository.save(new Artist("아이유", "www.IU-profileImage.png"));
            아이브 = artistRepository.save(new Artist("아이브", "www.IVE-profileImage.png"));
            아이들 = artistRepository.save(new Artist("아이들", "www.Idle-profileImage.png"));
            school = schoolRepository.save(new School("knu.ac.kr", "경북대", SchoolRegion.대구));
            var festivalA = festivalRepository.save(
                new Festival("축제", today.minusDays(1), today.plusDays(10L), school));

            var yesterdayDateTime = LocalDateTime.of(festivalA.getStartDate(), LocalTime.MIN);
            어제_공연 = stageRepository.save(new Stage(yesterdayDateTime, yesterdayDateTime.minusHours(1), festivalA));
            오늘_공연 = stageRepository.save(
                new Stage(yesterdayDateTime.plusDays(1), yesterdayDateTime.minusHours(1), festivalA));
            내일_공연 = stageRepository.save(
                new Stage(yesterdayDateTime.plusDays(2), yesterdayDateTime.minusHours(1), festivalA));
        }

        @Test
        void 아티스트의_당일_및_예정_공연_갯수를_조회한다() {
            // given
            saveStageArtist(아이유, 오늘_공연);
            var 아이유_공연_갯수 = new ArtistSearchStageCountV1Response(1, 0);

            saveStageArtist(아이브, 오늘_공연);
            saveStageArtist(아이브, 내일_공연);
            var 아이브_공연_갯수 = new ArtistSearchStageCountV1Response(1, 1);

            saveStageArtist(아이들, 어제_공연);
            saveStageArtist(아이들, 내일_공연);
            var 아이들_공연_갯수 = new ArtistSearchStageCountV1Response(0, 1);

            List<Long> artistIds = List.of(아이브.getId(), 아이유.getId(), 아이들.getId());

            // when
            Map<Long, ArtistSearchStageCountV1Response> actual = artistSearchStageCountV1QueryService.findArtistsStageCountAfterDateTime(
                artistIds, LocalDateTime.of(today, LocalTime.MIN));

            assertSoftly(softly -> {
                softly.assertThat(actual.get(아이유.getId())).isEqualTo(아이유_공연_갯수);
                softly.assertThat(actual.get(아이브.getId())).isEqualTo(아이브_공연_갯수);
                softly.assertThat(actual.get(아이들.getId())).isEqualTo(아이들_공연_갯수);
            });
        }

        @Test
        void 아티스트가_오늘_이후_공연이_없으면_0개() {
            saveStageArtist(아이브, 어제_공연);
            List<Long> artistIds = List.of(아이브.getId());
            var 아이브_공연_갯수 = new ArtistSearchStageCountV1Response(0, 0);

            // when
            Map<Long, ArtistSearchStageCountV1Response> actual = artistSearchStageCountV1QueryService.findArtistsStageCountAfterDateTime(
                artistIds, LocalDateTime.of(today, LocalTime.MIN));

            // then
            assertThat(actual.get(아이브.getId())).isEqualTo(아이브_공연_갯수);
        }
    }

    private void saveStageArtist(Artist artist, Stage stage) {
        stageArtistRepository.save(new StageArtist(stage.getId(), artist.getId()));
    }
}
