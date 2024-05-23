package com.festago.festival.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.TimeInstantProvider;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
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

    @Autowired
    QueryDslSchoolUpcomingFestivalStartDateV1QueryService schoolUpcomingFestivalStartDateV1QueryService;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    Clock clock;

    School 테코대학교;

    School 우테대학교;

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
        테코대학교 = schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());
        우테대학교 = schoolRepository.save(SchoolFixture.builder().name("우테대학교").build());
        festivalRepository.save(FestivalFixture.builder()
            .name("테코대학교 6월 15일 당일 축제")
            .startDate(_6월_15일)
            .endDate(_6월_15일)
            .school(테코대학교)
            .build()
        );
        festivalRepository.save(FestivalFixture.builder()
            .name("테코대학교 6월 16일 당일 축제")
            .startDate(_6월_16일)
            .endDate(_6월_16일)
            .school(테코대학교)
            .build()
        );
        festivalRepository.save(FestivalFixture.builder()
            .name("우테대학교 6월 16~17일 축제")
            .startDate(_6월_16일)
            .endDate(_6월_17일)
            .school(우테대학교)
            .build()
        );
    }

    @Test
    void 오늘이_6월_14일_일때_테코대학교는_6월_15일_우테대학교는_6월_16일이_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_14일));

        // when
        var actual = schoolUpcomingFestivalStartDateV1QueryService.getSchoolIdToUpcomingFestivalStartDate(
            List.of(테코대학교.getId(), 우테대학교.getId())
        );

        // then
        assertThat(actual.get(테코대학교.getId())).isEqualTo(_6월_15일);
        assertThat(actual.get(우테대학교.getId())).isEqualTo(_6월_16일);
    }

    @Test
    void 오늘이_6월_15일_일때_테코대학교는_6월_15일_우테대학교는_6월_16일_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_15일));

        // when
        var actual = schoolUpcomingFestivalStartDateV1QueryService.getSchoolIdToUpcomingFestivalStartDate(
            List.of(테코대학교.getId(), 우테대학교.getId())
        );

        // then
        assertThat(actual.get(테코대학교.getId())).isEqualTo(_6월_15일);
        assertThat(actual.get(우테대학교.getId())).isEqualTo(_6월_16일);
    }

    @Test
    void 오늘이_6월_16일_일때_테코대학교는_6월_16일_우테대학교는_6월_16일_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_16일));

        // when
        var actual = schoolUpcomingFestivalStartDateV1QueryService.getSchoolIdToUpcomingFestivalStartDate(
            List.of(테코대학교.getId(), 우테대학교.getId())
        );

        // then
        assertThat(actual.get(테코대학교.getId())).isEqualTo(_6월_16일);
        assertThat(actual.get(우테대학교.getId())).isEqualTo(_6월_16일);
    }

    @Test
    void 오늘이_6월_17일_일때_테코대학교는_null_우테대학교는_6월_16일_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_17일));

        // when
        var actual = schoolUpcomingFestivalStartDateV1QueryService.getSchoolIdToUpcomingFestivalStartDate(
            List.of(테코대학교.getId(), 우테대학교.getId())
        );

        // then
        assertThat(actual.get(테코대학교.getId())).isNull();
        assertThat(actual.get(우테대학교.getId())).isEqualTo(_6월_16일);
    }

    @Test
    void 오늘이_6월_18일_일때_테코대학교는_null_우테대학교는_null_조회된다() {
        // given
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(_6월_18일));

        // when
        var actual = schoolUpcomingFestivalStartDateV1QueryService.getSchoolIdToUpcomingFestivalStartDate(
            List.of(테코대학교.getId(), 우테대학교.getId())
        );

        // then
        assertThat(actual.get(테코대학교.getId())).isNull();
        assertThat(actual.get(우테대학교.getId())).isNull();
    }
}
