package com.festago.festival.application.integration.query;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.dto.command.ArtistCreateCommand;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.application.FestivalDetailV1QueryService;
import com.festago.festival.application.command.FestivalCreateService;
import com.festago.festival.dto.SocialMediaV1Response;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.stage.application.command.StageCreateService;
import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.SocialMediaFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
class FestivalDetailV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalDetailV1QueryService festivalDetailV1QueryService;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    @Autowired
    SchoolCommandService schoolCommandService;

    @Autowired
    FestivalCreateService festivalCreateService;

    @Autowired
    StageCreateService stageCreateService;

    @Autowired
    ArtistCommandService artistCommandService;

    LocalDate now = LocalDate.parse("2077-06-30");

    Long 테코대학교_축제_식별자;
    Long 테코대학교_공연_없는_축제_식별자;
    Long 우테대학교_축제_식별자;

    /**
     * 테코대학교 축제는 공연이 있는 3일 기간의 축제와 공연이 없는 당일 축제가 있다.<br/> 테코대학교는 소셜미디어에 인스타그램과 페이스북이 등록되어 있다.<br/> <br/> 우테대학교 축제는 공연이
     * 있는 당일 축제가 있다.<br/> 우테대학교에는 소셜미디어가 등록되어 있지 않다.<br/>
     */
    @BeforeEach
    void setUp() {
        Long 테코대학교_식별자 = createSchool("테코대학교", "teco.ac.kr");
        Long 우테대학교_식별자 = createSchool("우테대학교", "wote.ac.kr");

        테코대학교_축제_식별자 = festivalCreateService.createFestival(new FestivalCreateCommand(
            "테코대학교 축제", now, now.plusDays(2), "https://school.com/image.com", 테코대학교_식별자
        ));
        테코대학교_공연_없는_축제_식별자 = festivalCreateService.createFestival(new FestivalCreateCommand(
            "테코대학교 공연 없는 축제", now, now, "https://school.com/image.com", 테코대학교_식별자
        ));
        우테대학교_축제_식별자 = festivalCreateService.createFestival(new FestivalCreateCommand(
            "우테대학교 축제", now, now, "https://school.com/image.com", 우테대학교_식별자
        ));

        Long 아티스트_식별자 = createArtist("아티스트A");

        LocalDateTime ticketOpenTime = now.minusWeeks(1).atStartOfDay();
        stageCreateService.createStage(new StageCreateCommand(
            테코대학교_축제_식별자, now.atTime(18, 0), ticketOpenTime, List.of(아티스트_식별자)
        ));
        stageCreateService.createStage(new StageCreateCommand(
            테코대학교_축제_식별자, now.plusDays(1).atTime(18, 0), ticketOpenTime, List.of(아티스트_식별자)
        ));
        stageCreateService.createStage(new StageCreateCommand(
            테코대학교_축제_식별자, now.plusDays(2).atTime(18, 0), ticketOpenTime, List.of(아티스트_식별자)
        ));
        stageCreateService.createStage(new StageCreateCommand(
            우테대학교_축제_식별자, now.atTime(18, 0), ticketOpenTime, List.of(아티스트_식별자)
        ));

        socialMediaRepository.save(SocialMediaFixture.builder()
            .ownerId(테코대학교_식별자)
            .ownerType(OwnerType.SCHOOL)
            .mediaType(SocialMediaType.INSTAGRAM)
            .name("총학생회 인스타그램")
            .logoUrl("https://logo.com/instagram.png")
            .url("https://instagram.com/테코대학교_총학생회")
            .build());
        socialMediaRepository.save(SocialMediaFixture.builder()
            .ownerId(테코대학교_식별자)
            .ownerType(OwnerType.SCHOOL)
            .mediaType(SocialMediaType.FACEBOOK)
            .name("총학생회 페이스북")
            .logoUrl("https://logo.com/instagram.png")
            .url("https://facebook.com/테코대학교_총학생회")
            .build()
        );
    }

    private Long createArtist(String artistName) {
        return artistCommandService.save(new ArtistCreateCommand(
            artistName, "https://image.com/profileImage.png", "https://image.com/background.png"
        ));
    }

    private Long createSchool(String schoolName, String domain) {
        return schoolCommandService.createSchool(new SchoolCreateCommand(
            schoolName, domain, SchoolRegion.서울, "https://image.com/logo.png", "https://image.com/background.png"
        ));
    }

    @Test
    void 축제의_식별자로_축제의_상세_조회를_할_수_있다() {
        // when
        var response = festivalDetailV1QueryService.findFestivalDetail(테코대학교_축제_식별자);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.id()).isEqualTo(테코대학교_축제_식별자);
            softly.assertThat(response.startDate()).isEqualTo("2077-06-30");
            softly.assertThat(response.endDate()).isEqualTo("2077-07-02");
            softly.assertThat(response.posterImageUrl()).isEqualTo("https://school.com/image.com");
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
        var response = festivalDetailV1QueryService.findFestivalDetail(테코대학교_공연_없는_축제_식별자);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.id()).isEqualTo(테코대학교_공연_없는_축제_식별자);
            softly.assertThat(response.stages()).isEmpty();
            softly.assertThat(response.socialMedias())
                .map(SocialMediaV1Response::name)
                .containsExactlyInAnyOrder("총학생회 인스타그램", "총학생회 페이스북");
        });
    }

    @Test
    void 축제에_속한_학교에_소셜미디어가_없으면_소셜미디어에는_비어있는_컬렉션이_반환된다() {
        // when
        var response = festivalDetailV1QueryService.findFestivalDetail(우테대학교_축제_식별자);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.id()).isEqualTo(우테대학교_축제_식별자);
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
