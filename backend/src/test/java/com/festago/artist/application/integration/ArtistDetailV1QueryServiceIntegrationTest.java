package com.festago.artist.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import com.festago.artist.application.ArtistDetailV1QueryService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistFestivalDetailV1Response;
import com.festago.artist.dto.ArtistMediaV1Response;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.TimeInstantProvider;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.SocialMediaFixture;
import com.festago.support.fixture.StageArtistFixture;
import com.festago.support.fixture.StageFixture;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistDetailV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    Clock clock;

    @Autowired
    ArtistDetailV1QueryService artistDetailV1QueryService;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    StageArtistRepository stageArtistRepository;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    @Nested
    class 아티스트_상세_정보_조회 {

        @Test
        void 조회할_수_있다() {
            // given
            Long 아티스트_식별자 = createArtist("pooh").getId();
            makeSocialMedia(아티스트_식별자, OwnerType.ARTIST, SocialMediaType.INSTAGRAM);
            makeSocialMedia(아티스트_식별자, OwnerType.ARTIST, SocialMediaType.YOUTUBE);

            // when
            var actual = artistDetailV1QueryService.findArtistDetail(아티스트_식별자);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.id()).isEqualTo(아티스트_식별자);
                softly.assertThat(actual.socialMedias()).hasSize(2);
            });
        }

        @Test
        void 소셜_미디어가_없어도_조회할_수_있다() {
            // given
            Long 아티스트_식별자 = createArtist("pooh").getId();

            // when
            var actual = artistDetailV1QueryService.findArtistDetail(아티스트_식별자);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.id()).isEqualTo(아티스트_식별자);
                softly.assertThat(actual.socialMedias()).isEmpty();
            });
        }

        @Test
        void 소셜_미디어의_주인_아이디가_같더라도_주인의_타입에_따라_구분하여_조회한다() {
            // given
            Long 아티스트_식별자 = createArtist("pooh").getId();
            makeSocialMedia(아티스트_식별자, OwnerType.ARTIST, SocialMediaType.INSTAGRAM);

            // when
            makeSocialMedia(아티스트_식별자, OwnerType.SCHOOL, SocialMediaType.YOUTUBE);
            var actual = artistDetailV1QueryService.findArtistDetail(아티스트_식별자);

            // then
            assertThat(actual.socialMedias())
                .map(ArtistMediaV1Response::type)
                .containsExactly(SocialMediaType.INSTAGRAM.name());
        }

        @Test
        void 존재하지_않는_아티스트를_검색하면_에외() {
            // given & when & then
            assertThatThrownBy(() -> artistDetailV1QueryService.findArtistDetail(4885L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ARTIST_NOT_FOUND.getMessage());
        }

        Long makeSocialMedia(Long ownerId, OwnerType ownerType, SocialMediaType socialMediaType) {
            var socialMedia = SocialMediaFixture.builder()
                .ownerId(ownerId)
                .ownerType(ownerType)
                .mediaType(socialMediaType)
                .build();
            return socialMediaRepository.save(socialMedia).getId();
        }
    }

    /**
     * 현재 시간은 6월 15일 18시 0분이다.<br/> <br/> 각 축제는 다음과 같이 진행 된다.<br/> 6월 14일~14일 서울대학교 축제<br/> 6월 15일~15일 부산대학교 축제<br/> 6월
     * 16일~16일 대구대학교 축제<br/> <br/> 서울대학교 축제는 종료된 상태이다.<br/> 부산대학교 축제는 진행 중 상태이다.<br/> 대구대학교 축제는 진행 예정 상태이다.<br/> <br/>
     * 아티스트A는 위 세 축제의 공연에 참여한 상태이다.
     */
    @Nested
    class 아티스트가_참여한_축제_목록_조회 {

        LocalDateTime now = LocalDateTime.parse("2077-06-15T18:00:00");
        LocalDate _6월_14일 = LocalDate.parse("2077-06-14");
        LocalDate _6월_15일 = LocalDate.parse("2077-06-15");
        LocalDate _6월_16일 = LocalDate.parse("2077-06-16");

        Artist 아티스트A;

        Festival 서울대학교_축제;
        Festival 부산대학교_축제;
        Festival 대구대학교_축제;

        @BeforeEach
        void setUp() {
            School 서울대학교 = createSchool("서울대학교", "seoul.ac.kr", SchoolRegion.서울);
            School 부산대학교 = createSchool("부산대학교", "busan.ac.kr", SchoolRegion.부산);
            School 대구대학교 = createSchool("대구대학교", "daegu.ac.kr", SchoolRegion.대구);

            서울대학교_축제 = createFestival("서울대학교 축제", _6월_14일, _6월_14일, 서울대학교);
            부산대학교_축제 = createFestival("부산대학교 축제", _6월_15일, _6월_15일, 부산대학교);
            대구대학교_축제 = createFestival("대구대학교 축제", _6월_16일, _6월_16일, 대구대학교);

            아티스트A = createArtist("아티스트A");

            createStage(서울대학교_축제, _6월_14일.atTime(18, 0), 아티스트A);
            createStage(부산대학교_축제, _6월_15일.atTime(18, 0), 아티스트A);
            createStage(대구대학교_축제, _6월_16일.atTime(18, 0), 아티스트A);

            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
        }

        private Festival createFestival(String festivalName, LocalDate startDate, LocalDate endDate, School school) {
            return festivalRepository.save(FestivalFixture.builder()
                .name(festivalName)
                .startDate(startDate)
                .endDate(endDate)
                .school(school)
                .build()
            );
        }

        private void createStage(Festival festival, LocalDateTime startTime, Artist... artists) {
            Stage stage = stageRepository.save(StageFixture.builder()
                .festival(festival)
                .startTime(startTime)
                .build()
            );
            for (Artist artist : artists) {
                stageArtistRepository.save(StageArtistFixture.builder(stage.getId(), artist.getId()).build());
            }
        }

        @Test
        void 진행중인_축제_조회가_가능하다() {
            // given & when
            var actual = artistDetailV1QueryService.findArtistFestivals(
                아티스트A.getId(),
                null,
                null,
                false,
                PageRequest.ofSize(10)
            );

            // then
            assertThat(actual.getContent())
                .map(ArtistFestivalDetailV1Response::id)
                .containsExactly(부산대학교_축제.getId(), 대구대학교_축제.getId());
        }

        @Test
        void 종료된_축제_조회가_가능하다() {
            // given & when
            var actual = artistDetailV1QueryService.findArtistFestivals(
                아티스트A.getId(),
                null,
                null,
                true,
                PageRequest.ofSize(10)
            );

            // then
            assertThat(actual.getContent())
                .map(ArtistFestivalDetailV1Response::id)
                .containsExactly(서울대학교_축제.getId());
        }

        @Test
        void 커서_기반_페이징이_가능하다() {
            // given
            var firstResponse = artistDetailV1QueryService.findArtistFestivals(
                아티스트A.getId(),
                null,
                null,
                false,
                PageRequest.ofSize(1)
            );

            var firstFestivalResponse = firstResponse.getContent().get(0);

            // when
            var secondResponse = artistDetailV1QueryService.findArtistFestivals(
                아티스트A.getId(),
                firstFestivalResponse.id(),
                firstFestivalResponse.startDate(),
                false,
                PageRequest.ofSize(1)
            );

            // then
            assertThat(secondResponse.getContent())
                .map(ArtistFestivalDetailV1Response::id)
                .containsExactly(대구대학교_축제.getId());
        }
    }

    private School createSchool(String schoolName, String domain, SchoolRegion region) {
        return schoolRepository.save(SchoolFixture.builder()
            .name(schoolName)
            .domain(domain)
            .region(region)
            .build()
        );
    }

    private Artist createArtist(String artistName) {
        return artistRepository.save(ArtistFixture.builder()
            .name(artistName)
            .build()
        );
    }
}
