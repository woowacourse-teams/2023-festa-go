package com.festago.artist.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.artist.domain.Artist;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.StageFixture;
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
import org.springframework.boot.test.context.SpringBootTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class ArtistV1SearchQueryDslRepositoryTest {

    @Autowired
    private ArtistV1SearchQueryDslRepository artistV1SearchQueryDslRepository;

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

        Artist artistA;
        Artist artistB;
        Artist artistC;
        Festival festival;
        LocalDate today = LocalDate.now();
        School school;

        @BeforeEach
        void setUp() {
            artistA = artistRepository.save(new Artist("에이핑크", "image"));
            artistB = artistRepository.save(new Artist("블랙핑크", "image"));
            artistC = artistRepository.save(new Artist("핑크 플로이드", "image"));
            school = schoolRepository.save(new School("knu.ac.kr", "경북대", SchoolRegion.대구));
            festival = festivalRepository.save(new Festival("축제", today, today.plusDays(10L), school));
            
        }

        @Test
        void 아티스트의_당일_및_예정_공연시간을_조회한다() {
            // given - 조회 가능
            LocalDateTime todayNoon = LocalDateTime.of(today, LocalTime.NOON);
            Stage stageA = stageRepository.save(StageFixture.stage().startTime(todayNoon).festival(festival).build());
            saveStageArtist(stageA, artistA);
            saveStageArtist(stageA, artistB);
            Stage stageB = stageRepository.save(StageFixture.stage().startTime(todayNoon.plusHours(1)).festival(festival).build());
            saveStageArtist(stageB, artistA);
            saveStageArtist(stageB, artistB);

            //끝난 공연 및 축제 - 조회 불가
            LocalDate endFestivalStartDate = today.minusDays(10);
            LocalDateTime endStageStartTime = LocalDateTime.of(endFestivalStartDate, LocalTime.MIDNIGHT);
            Festival endFestival = festivalRepository.save(
                new Festival("축제", endFestivalStartDate, today.minusDays(5), school));
            Stage endStage = stageRepository.save(
                new Stage(endStageStartTime, endStageStartTime.minusHours(10), endFestival));
            saveStageArtist(endStage, artistA);
            saveStageArtist(endStage, artistB);
            saveStageArtist(endStage, artistC);


            // when
            List<Long> artistIds = List.of(artistA.getId(), artistB.getId(), artistC.getId());
            Map<Long, List<LocalDateTime>> actual = artistV1SearchQueryDslRepository.findArtistsStageStartTimeAfterDate(artistIds, today);

            // then
            assertThat(actual.get(artistA.getId())).hasSize(2);
            assertThat(actual.get(artistB.getId())).hasSize(2);
            assertThat(actual.get(artistC.getId())).hasSize(0);
        }
    }

    private void saveStageArtist(Stage stage, Artist artist) {
        stageArtistRepository.save(new StageArtist(stage.getId(), artist.getId()));
    }

}
