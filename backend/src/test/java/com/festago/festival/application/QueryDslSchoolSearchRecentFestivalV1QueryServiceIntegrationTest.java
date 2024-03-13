package com.festago.festival.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.festago.festival.application.command.FestivalCreateService;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.TimeInstantProvider;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QueryDslSchoolSearchRecentFestivalV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    private static final String LOGO_URL = "https://image.com/logo.png";
    private static final String BACKGROUND_IMAGE_URL = "https://image.com/backgroundimage.png";
    private static final String POSTER_IMAGE_URL = "https://image.com/posterimage.png";

    @Autowired
    QueryDslSchoolSearchUpcomingFestivalV1QueryService queryDslSchoolSearchRecentFestivalV1QueryService;

    @Autowired
    SchoolCommandService schoolCommandService;

    @Autowired
    FestivalCreateService festivalCreateService;

    @Autowired
    Clock clock;

    Long 테코대학교_식별자;

    Long 우테대학교_식별자;

    LocalDate _6월_14일 = LocalDate.parse("2077-06-14");
    LocalDate _6월_15일 = LocalDate.parse("2077-06-15");
    LocalDate _6월_16일 = LocalDate.parse("2077-06-16");
    LocalDate _6월_17일 = LocalDate.parse("2077-06-17");
    LocalDate _6월_18일 = LocalDate.parse("2077-06-18");

    /**
     * 테코대학교에 6월 15일 ~ 6월 15일 축제, 6월 16일 ~ 6월 16일 축제 우테대학교에 6월 16일 ~ 6월 17일 축제
     */
    @BeforeEach
    void setUp() {
        테코대학교_식별자 = schoolCommandService.createSchool(
            new SchoolCreateCommand("테코대학교", "teco.ac.kr", SchoolRegion.서울, LOGO_URL, BACKGROUND_IMAGE_URL)
        );
        우테대학교_식별자 = schoolCommandService.createSchool(
            new SchoolCreateCommand("우테대학교", "wote.ac.kr", SchoolRegion.서울, LOGO_URL, BACKGROUND_IMAGE_URL)
        );
        festivalCreateService.createFestival(
            new FestivalCreateCommand("테코대학교 6월 15일 당일 축제", _6월_15일, _6월_15일, POSTER_IMAGE_URL, 테코대학교_식별자)
        );
        festivalCreateService.createFestival(
            new FestivalCreateCommand("테코대학교 6월 16일 당일 축제", _6월_16일, _6월_16일, POSTER_IMAGE_URL, 테코대학교_식별자)
        );
        festivalCreateService.createFestival(
            new FestivalCreateCommand("우테대학교 6월 16~17일 축제", _6월_16일, _6월_17일, POSTER_IMAGE_URL, 우테대학교_식별자)
        );
    }

    @Test
    void 오늘이_6월_14일_일때_테코대학교는_6월_15일_우테대학교는_6월_16일이_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_14일));

        // when
        var actual = queryDslSchoolSearchRecentFestivalV1QueryService.searchUpcomingFestivals(
            List.of(테코대학교_식별자, 우테대학교_식별자)
        );

        // then
        assertThat(actual.get(테코대학교_식별자).startDate()).isEqualTo(_6월_15일);
        assertThat(actual.get(우테대학교_식별자).startDate()).isEqualTo(_6월_16일);
    }

    @Test
    void 오늘이_6월_15일_일때_테코대학교는_6월_15일_우테대학교는_6월_16일_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_15일));

        // when
        var actual = queryDslSchoolSearchRecentFestivalV1QueryService.searchUpcomingFestivals(
            List.of(테코대학교_식별자, 우테대학교_식별자)
        );

        // then
        assertThat(actual.get(테코대학교_식별자).startDate()).isEqualTo(_6월_15일);
        assertThat(actual.get(우테대학교_식별자).startDate()).isEqualTo(_6월_16일);
    }

    @Test
    void 오늘이_6월_16일_일때_테코대학교는_6월_16일_우테대학교는_6월_16일_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_16일));

        // when
        var actual = queryDslSchoolSearchRecentFestivalV1QueryService.searchUpcomingFestivals(
            List.of(테코대학교_식별자, 우테대학교_식별자)
        );

        // then
        assertThat(actual.get(테코대학교_식별자).startDate()).isEqualTo(_6월_16일);
        assertThat(actual.get(우테대학교_식별자).startDate()).isEqualTo(_6월_16일);
    }

    @Test
    void 오늘이_6월_17일_일때_테코대학교는_null_우테대학교는_6월_16일_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_17일));

        // when
        var actual = queryDslSchoolSearchRecentFestivalV1QueryService.searchUpcomingFestivals(
            List.of(테코대학교_식별자, 우테대학교_식별자)
        );

        // then
        assertThat(actual.get(테코대학교_식별자)).isNull();
        assertThat(actual.get(우테대학교_식별자).startDate()).isEqualTo(_6월_16일);
    }

    @Test
    void 오늘이_6월_18일_일때_테코대학교는_null_우테대학교는_null_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_18일));

        // when
        var actual = queryDslSchoolSearchRecentFestivalV1QueryService.searchUpcomingFestivals(
            List.of(테코대학교_식별자, 우테대학교_식별자)
        );

        // then
        assertThat(actual.get(테코대학교_식별자)).isNull();
        assertThat(actual.get(우테대학교_식별자)).isNull();
    }
}
