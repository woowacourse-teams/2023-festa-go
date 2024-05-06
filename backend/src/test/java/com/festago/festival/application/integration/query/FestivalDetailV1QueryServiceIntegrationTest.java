package com.festago.festival.application.integration.query;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.application.FestivalDetailV1QueryService;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.SocialMediaV1Response;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.stage.application.command.StageCreateService;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.SocialMediaFixture;
import com.festago.support.fixture.StageArtistFixture;
import com.festago.support.fixture.StageFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalDetailV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalDetailV1QueryService festivalDetailV1QueryService;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageCreateService stageCreateService;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    StageArtistRepository stageArtistRepository;

    @Autowired
    ArtistRepository artistRepository;

    LocalDate now = LocalDate.parse("2077-06-30");

    Festival 테코대학교_축제;
    Festival 테코대학교_공연_없는_축제;
    Festival 우테대학교_축제;

    /**
     * 테코대학교 축제는 공연이 있는 3일 기간의 축제와 공연이 없는 당일 축제가 있다.<br/> 테코대학교는 소셜미디어에 인스타그램과 페이스북이 등록되어 있다.<br/> <br/> 우테대학교 축제는 공연이
     * 있는 당일 축제가 있다.<br/> 우테대학교에는 소셜미디어가 등록되어 있지 않다.<br/>
     */
    @BeforeEach
    void setUp() {
        School 테코대학교 = createSchool("테코대학교", "teco.ac.kr");
        School 우테대학교 = createSchool("우테대학교", "wote.ac.kr");

        테코대학교_축제 = festivalRepository.save(FestivalFixture.builder()
            .startDate(now)
            .endDate(now.plusDays(2))
            .school(테코대학교)
            .build()
        );
        테코대학교_공연_없는_축제 = festivalRepository.save(FestivalFixture.builder()
            .startDate(now)
            .endDate(now)
            .school(테코대학교)
            .build()
        );
        우테대학교_축제 = festivalRepository.save(FestivalFixture.builder()
            .startDate(now)
            .endDate(now)
            .school(우테대학교)
            .build()
        );
        Artist 아티스트A = createArtist("아티스트A");

        Stage 테코대학교_축제_1일차_공연 = stageRepository.save(StageFixture.builder()
            .festival(테코대학교_축제)
            .startTime(now.atTime(18, 0))
            .build()
        );
        Stage 테코대학교_축제_2일차_공연 = stageRepository.save(StageFixture.builder()
            .festival(테코대학교_축제)
            .startTime(now.plusDays(1).atTime(18, 0))
            .build()
        );
        Stage 테코대학교_축제_3일차_공연 = stageRepository.save(StageFixture.builder()
            .festival(테코대학교_축제)
            .startTime(now.plusDays(2).atTime(18, 0))
            .build()
        );
        Stage 우테대학교_축제_당일_공연 = stageRepository.save(StageFixture.builder()
            .festival(우테대학교_축제)
            .startTime(now.atTime(18, 0))
            .build()
        );
        stageArtistRepository.save(StageArtistFixture.builder(테코대학교_축제_1일차_공연.getId(), 아티스트A.getId()).build());
        stageArtistRepository.save(StageArtistFixture.builder(테코대학교_축제_2일차_공연.getId(), 아티스트A.getId()).build());
        stageArtistRepository.save(StageArtistFixture.builder(테코대학교_축제_3일차_공연.getId(), 아티스트A.getId()).build());
        stageArtistRepository.save(StageArtistFixture.builder(우테대학교_축제_당일_공연.getId(), 아티스트A.getId()).build());

        socialMediaRepository.save(SocialMediaFixture.builder()
            .ownerId(테코대학교.getId())
            .ownerType(OwnerType.SCHOOL)
            .mediaType(SocialMediaType.INSTAGRAM)
            .name("총학생회 인스타그램")
            .logoUrl("https://logo.com/instagram.png")
            .url("https://instagram.com/테코대학교_총학생회")
            .build());
        socialMediaRepository.save(SocialMediaFixture.builder()
            .ownerId(테코대학교.getId())
            .ownerType(OwnerType.SCHOOL)
            .mediaType(SocialMediaType.FACEBOOK)
            .name("총학생회 페이스북")
            .logoUrl("https://logo.com/instagram.png")
            .url("https://facebook.com/테코대학교_총학생회")
            .build()
        );
    }

    private Artist createArtist(String artistName) {
        Artist artist = ArtistFixture.builder()
            .name(artistName)
            .build();
        return artistRepository.save(artist);
    }

    private School createSchool(String schoolName, String domain) {
        School school = SchoolFixture.builder()
            .name(schoolName)
            .domain(domain)
            .build();
        return schoolRepository.save(school);
    }

    @Test
    void 축제의_식별자로_축제의_상세_조회를_할_수_있다() {
        // when
        var response = festivalDetailV1QueryService.findFestivalDetail(테코대학교_축제.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.id()).isEqualTo(테코대학교_축제.getId());
            softly.assertThat(response.startDate()).isEqualTo("2077-06-30");
            softly.assertThat(response.endDate()).isEqualTo("2077-07-02");
            softly.assertThat(response.school().name()).isEqualTo("테코대학교");
            softly.assertThat(response.socialMedias())
                .map(SocialMediaV1Response::name)
                .containsExactlyInAnyOrder("총학생회 인스타그램", "총학생회 페이스북");
            softly.assertThat(response.stages()).hasSize(3);
        });
    }

    @Test
    void 축제에_공연이_없으면_응답의_공연에는_비어있는_컬렉션이_반환된다() {
        // when
        var response = festivalDetailV1QueryService.findFestivalDetail(테코대학교_공연_없는_축제.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.id()).isEqualTo(테코대학교_공연_없는_축제.getId());
            softly.assertThat(response.stages()).isEmpty();
            softly.assertThat(response.socialMedias())
                .map(SocialMediaV1Response::name)
                .containsExactlyInAnyOrder("총학생회 인스타그램", "총학생회 페이스북");
        });
    }

    @Test
    void 축제에_속한_학교에_소셜미디어가_없으면_소셜미디어에는_비어있는_컬렉션이_반환된다() {
        // when
        var response = festivalDetailV1QueryService.findFestivalDetail(우테대학교_축제.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.id()).isEqualTo(우테대학교_축제.getId());
            softly.assertThat(response.socialMedias()).isEmpty();
            softly.assertThat(response.stages()).hasSize(1);
        });
    }

    @Test
    void 축제의_식별자에_대한_축제가_없으면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> festivalDetailV1QueryService.findFestivalDetail(4885L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorCode.FESTIVAL_NOT_FOUND.getMessage());
    }
}
