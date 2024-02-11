package com.festago.festival.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.SocialMediaV1Response;
import com.festago.festival.dto.StageV1Response;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.repository.StageQueryInfoRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import java.time.LocalDate;
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
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    StageQueryInfoRepository stageQueryInfoRepository;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    LocalDate now = LocalDate.parse("2077-06-30");

    Festival 축제;

    @BeforeEach
    void setUp() {
        School 테코대학교 = schoolRepository.save(new School("teco.ac.kr", "테코대학교", SchoolRegion.서울));
        축제 = festivalRepository.save(
            new Festival("테코대학교 축제", now, now.plusDays(2), "https://school.com/image.com", 테코대학교));
        Stage 첫째날_공연 = stageRepository.save(
            new Stage(now.atTime(18, 0), "뉴진스", now.minusWeeks(1).atStartOfDay(), 축제));
        Stage 둘째날_공연 = stageRepository.save(
            new Stage(now.plusDays(1).atTime(18, 0), "에픽하이", now.minusWeeks(1).atStartOfDay(), 축제));
        Stage 셋째날_공연 = stageRepository.save(
            new Stage(now.plusDays(2).atTime(18, 0), "소녀시대", now.minusWeeks(1).atStartOfDay(), 축제));

        StageQueryInfo 첫째날_공연_QueryInfo = StageQueryInfo.create(첫째날_공연.getId());
        첫째날_공연_QueryInfo.updateArtist("뉴진스");
        stageQueryInfoRepository.save(첫째날_공연_QueryInfo);

        StageQueryInfo 둘째날_공연_QueryInfo = StageQueryInfo.create(둘째날_공연.getId());
        둘째날_공연_QueryInfo.updateArtist("에픽하이");
        stageQueryInfoRepository.save(둘째날_공연_QueryInfo);

        StageQueryInfo 셋째날_공연_QueryInfo = StageQueryInfo.create(셋째날_공연.getId());
        셋째날_공연_QueryInfo.updateArtist("소녀시대");
        stageQueryInfoRepository.save(셋째날_공연_QueryInfo);

        socialMediaRepository.save(
            new SocialMedia(테코대학교.getId(), OwnerType.SCHOOL, SocialMediaType.INSTAGRAM, "총학생회 인스타그램",
                "https://logo.com/instagram.png", "https://instagram.com/테코대학교_총학생회"));
        socialMediaRepository.save(
            new SocialMedia(테코대학교.getId(), OwnerType.SCHOOL, SocialMediaType.FACEBOOK, "총학생회 페이스북",
                "https://logo.com/facebook.png", "https://facebook.com/테코대학교_총학생회"));
    }

    @Test
    void 축제의_식별자로_축제의_상세_조회를_할_수_있다() throws Exception {
        // when & then
        var response = festivalDetailV1QueryService.findFestivalDetail(축제.getId());
        assertSoftly(softly -> {
            softly.assertThat(response.name()).isEqualTo("테코대학교 축제");
            softly.assertThat(response.startDate()).isEqualTo("2077-06-30");
            softly.assertThat(response.endDate()).isEqualTo("2077-07-02");
            softly.assertThat(response.imageUrl()).isEqualTo("https://school.com/image.com");
            softly.assertThat(response.school().name()).isEqualTo("테코대학교");
            softly.assertThat(response.socialMedias())
                .map(SocialMediaV1Response::name)
                .containsExactlyInAnyOrder("총학생회 인스타그램", "총학생회 페이스북");
            softly.assertThat(response.stages())
                .map(StageV1Response::artists)
                .containsExactlyInAnyOrder("뉴진스", "에픽하이", "소녀시대");
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
