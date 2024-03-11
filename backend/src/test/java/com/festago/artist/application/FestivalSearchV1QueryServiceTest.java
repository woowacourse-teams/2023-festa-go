package com.festago.artist.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.application.FestivalSearchV1QueryService;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalInfoSerializer;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.dto.FestivalSearchV1Response;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
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
    FestivalInfoSerializer festivalInfoSerializer;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    @Autowired
    FestivalSearchV1QueryService festivalSearchV1QueryService;

    Stage 부산_공연;
    Stage 서울_공연;
    Stage 대구_공연;

    @BeforeEach
    void setting() {
        LocalDate nowDate = LocalDate.now();
        LocalDateTime nowDateTime = LocalDateTime.now();

        School 부산_학교 = schoolRepository.save(new School("domain1", "부산 학교", SchoolRegion.부산));
        School 서울_학교 = schoolRepository.save(new School("domain2", "서울 학교", SchoolRegion.서울));
        School 대구_학교 = schoolRepository.save(new School("domain3", "대구 학교", SchoolRegion.대구));

        Festival 부산_축제 = festivalRepository.save(
            new Festival("부산대학교 축제", nowDate.minusDays(5), nowDate.minusDays(1), 부산_학교));
        Festival 서울_축제 = festivalRepository.save(
            new Festival("서울대학교 축제", nowDate.minusDays(1), nowDate.plusDays(3), 서울_학교));
        Festival 대구_축제 = festivalRepository.save(
            new Festival("대구대학교 축제", nowDate.plusDays(1), nowDate.plusDays(5), 대구_학교));

        festivalInfoRepository.save(FestivalQueryInfo.create(부산_축제.getId()));
        festivalInfoRepository.save(FestivalQueryInfo.create(서울_축제.getId()));
        festivalInfoRepository.save(FestivalQueryInfo.create(대구_축제.getId()));

        부산_공연 = stageRepository.save(new Stage(nowDateTime.minusDays(5L), nowDateTime.minusDays(6L), 부산_축제));
        서울_공연 = stageRepository.save(new Stage(nowDateTime.minusDays(1L), nowDateTime.minusDays(2L), 서울_축제));
        대구_공연 = stageRepository.save(new Stage(nowDateTime.plusDays(1L), nowDateTime, 대구_축제));
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
            assertThat(actual).hasSize(1);
        }

        @Test
        void 대학교_로_끝나는_검색은_학교_검색으로_들어간다() {
            // given
            String keyword = "부산대학교";

            // when
            List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search(keyword);

            // then
            assertThat(actual).hasSize(1);
        }
    }

    @Nested
    class 아티스트_기반_축제_검색에서 {

        @Nested
        class 두_글자_이상_라이크_검색은 {

            @Test
            void 키워드가_두_글자_이상일_때_해당_키워드를_가진_아티스트의_정보를_반환한다() {
                // given
                Artist 오리 = artistRepository.save(new Artist("오리", "image.jpg"));
                Artist 우푸우 = artistRepository.save(new Artist("우푸우", "image.jpg"));
                Artist 글렌 = artistRepository.save(new Artist("글렌", "image.jpg"));

                stageArtistRepository.save(new StageArtist(부산_공연.getId(), 오리.getId()));
                stageArtistRepository.save(new StageArtist(부산_공연.getId(), 우푸우.getId()));

                stageArtistRepository.save(new StageArtist(서울_공연.getId(), 오리.getId()));
                stageArtistRepository.save(new StageArtist(서울_공연.getId(), 글렌.getId()));

                stageArtistRepository.save(new StageArtist(대구_공연.getId(), 우푸우.getId()));

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("푸우");

                // then
                assertThat(actual).hasSize(2);
            }

            @Test
            void 해당하는_키워드의_아티스트가_없으면_빈_리스트을_반환한다() {
                // given
                Artist 오리 = artistRepository.save(new Artist("오리", "image.jpg"));
                Artist 우푸우 = artistRepository.save(new Artist("우푸우", "image.jpg"));
                Artist 글렌 = artistRepository.save(new Artist("글렌", "image.jpg"));

                stageArtistRepository.save(new StageArtist(부산_공연.getId(), 오리.getId()));
                stageArtistRepository.save(new StageArtist(부산_공연.getId(), 우푸우.getId()));

                stageArtistRepository.save(new StageArtist(서울_공연.getId(), 오리.getId()));
                stageArtistRepository.save(new StageArtist(서울_공연.getId(), 글렌.getId()));

                stageArtistRepository.save(new StageArtist(대구_공연.getId(), 우푸우.getId()));

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("렌글");

                // then
                assertThat(actual).isEmpty();
            }

            @Test
            void 아티스트가_공연에_참여하고_있지_않으면_빈_리스트가_반환된다() {
                // given
                Artist 우푸우 = artistRepository.save(new Artist("우푸우", "image.jpg"));

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
                Artist 푸우 = artistRepository.save(new Artist("푸우", "image.jpg"));
                Artist 푸 = artistRepository.save(new Artist("푸", "image.jpg"));

                stageArtistRepository.save(new StageArtist(부산_공연.getId(), 푸우.getId()));
                stageArtistRepository.save(new StageArtist(부산_공연.getId(), 푸.getId()));

                stageArtistRepository.save(new StageArtist(서울_공연.getId(), 푸.getId()));

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("푸");

                // then
                assertThat(actual).hasSize(2);
            }

            @Test
            void 해당하는_키워드의_아티스트가_없으면_빈_리스트를_반환한다() {
                // given
                Artist 푸우 = artistRepository.save(new Artist("푸우", "image.jpg"));
                Artist 푸푸푸푸 = artistRepository.save(new Artist("푸푸푸푸", "image.jpg"));
                Artist 글렌 = artistRepository.save(new Artist("글렌", "image.jpg"));

                stageArtistRepository.save(new StageArtist(부산_공연.getId(), 푸우.getId()));
                stageArtistRepository.save(new StageArtist(부산_공연.getId(), 푸푸푸푸.getId()));
                stageArtistRepository.save(new StageArtist(부산_공연.getId(), 글렌.getId()));

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("푸");

                // then
                assertThat(actual).isEmpty();
            }

            @Test
            void 아티스트가_공연에_참여하고_있지_않으면_빈_리스트를_반환한다() {
                // given
                Artist 우푸우 = artistRepository.save(new Artist("우푸우", "image.jpg"));

                // when
                List<FestivalSearchV1Response> actual = festivalSearchV1QueryService.search("우푸");

                // then
                assertThat(actual).isEmpty();
            }
        }
    }
}
