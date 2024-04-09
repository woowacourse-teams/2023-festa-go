package com.festago.artist.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.application.ArtistSearchStageCountV1QueryService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchStageCountV1Response;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.repository.FestivalRepository;
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
class ArtistSearchStageCountV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    ArtistSearchStageCountV1QueryService artistSearchStageCountV1QueryService;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    StageArtistRepository stageArtistRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Nested
    class 검색 {

        Artist 아이유;
        Artist 아이브;
        Artist 아이들;
        Stage _6월_15일_공연;
        Stage _6월_16일_공연;
        Stage _6월_17일_공연;
        LocalDate _6월_15일 = LocalDate.parse("2077-06-15");
        LocalDate _6월_16일 = LocalDate.parse("2077-06-16");
        LocalDate _6월_17일 = LocalDate.parse("2077-06-17");

        @BeforeEach
        void setUp() {
            아이유 = artistRepository.save(ArtistFixture.builder().name("아이유").build());
            아이브 = artistRepository.save(ArtistFixture.builder().name("아이브").build());
            아이들 = artistRepository.save(ArtistFixture.builder().name("아이들").build());
            var school = schoolRepository.save(
                SchoolFixture.builder()
                    .domain("knu.ac.kr")
                    .name("경북대")
                    .region(SchoolRegion.대구)
                    .build()
            );
            var festival = festivalRepository.save(
                FestivalFixture.builder()
                    .name("축제")
                    .startDate(_6월_15일)
                    .endDate(_6월_17일)
                    .school(school)
                    .build()
            );

            _6월_15일_공연 = stageRepository.save(StageFixture.builder()
                .startTime(_6월_15일.atStartOfDay())
                .festival(festival)
                .build()
            );
            _6월_16일_공연 = stageRepository.save(StageFixture.builder()
                .startTime(_6월_16일.atStartOfDay())
                .festival(festival)
                .build()
            );
            _6월_17일_공연 = stageRepository.save(StageFixture.builder()
                .startTime(_6월_17일.atStartOfDay())
                .festival(festival)
                .build()
            );
        }

        @Test
        void 아티스트의_당일_및_예정_공연_갯수를_조회한다() {
            // given
            LocalDateTime today = _6월_16일.atStartOfDay();

            saveStageArtist(아이유, _6월_16일_공연);
            var 아이유_공연_갯수 = new ArtistSearchStageCountV1Response(1, 0);

            saveStageArtist(아이브, _6월_16일_공연);
            saveStageArtist(아이브, _6월_17일_공연);
            var 아이브_공연_갯수 = new ArtistSearchStageCountV1Response(1, 1);

            saveStageArtist(아이들, _6월_15일_공연);
            saveStageArtist(아이들, _6월_17일_공연);
            var 아이들_공연_갯수 = new ArtistSearchStageCountV1Response(0, 1);

            // when
            List<Long> artistIds = List.of(아이브.getId(), 아이유.getId(), 아이들.getId());
            var actual = artistSearchStageCountV1QueryService.findArtistsStageCountAfterDateTime(
                artistIds, today
            );

            assertSoftly(softly -> {
                softly.assertThat(actual).containsEntry(아이유.getId(), 아이유_공연_갯수);
                softly.assertThat(actual).containsEntry(아이브.getId(), 아이브_공연_갯수);
                softly.assertThat(actual).containsEntry(아이들.getId(), 아이들_공연_갯수);
            });
        }

        @Test
        void 아티스트가_오늘_이후_공연이_없으면_0개() {
            LocalDateTime today = _6월_16일.atStartOfDay();

            saveStageArtist(아이브, _6월_15일_공연);
            var 아이브_공연_갯수 = new ArtistSearchStageCountV1Response(0, 0);

            // when
            List<Long> artistIds = List.of(아이브.getId());
            var actual = artistSearchStageCountV1QueryService.findArtistsStageCountAfterDateTime(
                artistIds, today
            );

            // then
            assertThat(actual).containsEntry(아이브.getId(), 아이브_공연_갯수);
        }
    }

    private void saveStageArtist(Artist artist, Stage stage) {
        stageArtistRepository.save(StageArtistFixture.builder(stage.getId(), artist.getId()).build());
    }
}
