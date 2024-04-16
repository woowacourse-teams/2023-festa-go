package com.festago.school.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.festago.festival.repository.FestivalRepository;
import com.festago.school.application.v2.SchoolFestivalsV2QueryService;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.school.repository.v2.SchoolFestivalsV2QueryDslRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.TimeInstantProvider;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import java.time.Clock;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolFestivalsV2CacheConfigTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolFestivalsV2QueryService schoolFestivalsV2QueryService;

    @Autowired
    SchoolFestivalsV2QueryDslRepository schoolFestivalsV2QueryDslRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    Clock clock;

    School 테코대학교;
    LocalDate _6월_15일 = LocalDate.parse("2077-06-15");
    LocalDate _6월_16일 = LocalDate.parse("2077-06-16");

    @BeforeEach
    void setUp() {
        테코대학교 = schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());
        festivalRepository.save(
            FestivalFixture.builder().startDate(_6월_15일).endDate(_6월_15일).school(테코대학교).build()
        );
    }

    @Nested
    class findFestivalsBySchoolId {

        @Test
        void 캐싱이_적용되어야_한다() {
            // given
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(_6월_15일));
            var expect = schoolFestivalsV2QueryService.findFestivalsBySchoolId(테코대학교.getId());

            // when
            festivalRepository.save(
                FestivalFixture.builder().startDate(_6월_15일).endDate(_6월_15일).school(테코대학교).build()
            );
            var actual = schoolFestivalsV2QueryService.findFestivalsBySchoolId(테코대학교.getId());

            // then
            assertThat(actual).isEqualTo(expect);
        }
    }

    @Nested
    class findPastFestivalsBySchoolId {

        @Test
        void 캐싱이_적용되어야_한다() {
            // given
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(_6월_16일));
            var expect = schoolFestivalsV2QueryService.findPastFestivalsBySchoolId(테코대학교.getId());

            // when
            festivalRepository.save(
                FestivalFixture.builder().startDate(_6월_15일).endDate(_6월_15일).school(테코대학교).build()
            );
            var actual = schoolFestivalsV2QueryService.findPastFestivalsBySchoolId(테코대학교.getId());

            // then
            assertThat(actual).isEqualTo(expect);
        }
    }
}
