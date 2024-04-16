package com.festago.school.application.v2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.SchoolFestivalV1Response;
import com.festago.school.repository.SchoolRepository;
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
class SchoolFestivalsV2QueryServiceTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolFestivalsV2QueryService schoolFestivalsV2QueryService;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    Clock clock;

    School 테코대학교;
    School 우테대학교;
    Festival 테코대학교_6월_15일_6월_15일_축제;
    Festival 테코대학교_6월_16일_6월_17일_축제;
    Festival 테코대학교_6월_17일_6월_18일_축제;
    Festival 테코대학교_6월_19일_6월_20일_축제;
    LocalDate _6월_15일 = LocalDate.parse("2077-06-15");
    LocalDate _6월_16일 = LocalDate.parse("2077-06-16");
    LocalDate _6월_17일 = LocalDate.parse("2077-06-17");
    LocalDate _6월_18일 = LocalDate.parse("2077-06-18");
    LocalDate _6월_19일 = LocalDate.parse("2077-06-19");
    LocalDate _6월_20일 = LocalDate.parse("2077-06-20");
    LocalDate _6월_21일 = LocalDate.parse("2077-06-21");

    /**
     * 축제는 다음과 같이 존재한다. <br/> 테코대학교 6월 15일 ~ 6월 15일 <br/> 테코대학교 6월 16일 ~ 6월 17일 <br/> 테코대학교 6월 17일 ~ 6월 18일 <br/> 테코대학교
     * 6월 19일 ~ 6월 20일 <br/> 또한 우테대학교에는 축제가 존재하지 않는다 <br/>
     */
    @BeforeEach
    void setUp() {
        테코대학교 = schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());
        우테대학교 = schoolRepository.save(SchoolFixture.builder().name("우테대학교").build());
        테코대학교_6월_15일_6월_15일_축제 = festivalRepository.save(
            FestivalFixture.builder().startDate(_6월_15일).endDate(_6월_15일).school(테코대학교).build()
        );
        테코대학교_6월_19일_6월_20일_축제 = festivalRepository.save(
            FestivalFixture.builder().startDate(_6월_19일).endDate(_6월_20일).school(테코대학교).build()
        );
        테코대학교_6월_17일_6월_18일_축제 = festivalRepository.save(
            FestivalFixture.builder().startDate(_6월_17일).endDate(_6월_18일).school(테코대학교).build()
        );
        테코대학교_6월_16일_6월_17일_축제 = festivalRepository.save(
            FestivalFixture.builder().startDate(_6월_16일).endDate(_6월_17일).school(테코대학교).build()
        );
    }

    @Nested
    class findFestivalsBySchoolId {

        @Test
        void 진행_중_진행_예정_축제가_없으면_빈_리스트가_반환된다() {
            // given
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(_6월_15일));

            // when
            var actual = schoolFestivalsV2QueryService.findFestivalsBySchoolId(우테대학교.getId());

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 진행_중_진행_예정_축제_조회는_시작일_오름차순으로_정렬된다() {
            // given
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(_6월_15일));

            // when
            var actual = schoolFestivalsV2QueryService.findFestivalsBySchoolId(테코대학교.getId());

            // then
            assertThat(actual)
                .map(SchoolFestivalV1Response::id)
                .containsExactly(
                    테코대학교_6월_15일_6월_15일_축제.getId(),
                    테코대학교_6월_16일_6월_17일_축제.getId(),
                    테코대학교_6월_17일_6월_18일_축제.getId(),
                    테코대학교_6월_19일_6월_20일_축제.getId()
                );
        }

        @Test
        void 진행_중_진행_예정_축제는_종료일을_포함한다() {
            // given
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(_6월_18일));

            // when
            var actual = schoolFestivalsV2QueryService.findFestivalsBySchoolId(테코대학교.getId());

            // then
            assertThat(actual)
                .map(SchoolFestivalV1Response::id)
                .containsExactly(
                    테코대학교_6월_17일_6월_18일_축제.getId(),
                    테코대학교_6월_19일_6월_20일_축제.getId()
                );
        }
    }

    @Nested
    class findPastFestivalsBySchoolId {

        @Test
        void 과거_축제가_없으면_빈_리스트가_반환된다() {
            // given
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(_6월_15일));

            // when
            var actual = schoolFestivalsV2QueryService.findPastFestivalsBySchoolId(우테대학교.getId());

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 과거_축제_조회는_종료일_내림차순으로_정렬된다() {
            // given
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(_6월_21일));

            // when
            var actual = schoolFestivalsV2QueryService.findPastFestivalsBySchoolId(테코대학교.getId());

            // then
            assertThat(actual)
                .map(SchoolFestivalV1Response::id)
                .containsExactly(
                    테코대학교_6월_19일_6월_20일_축제.getId(),
                    테코대학교_6월_17일_6월_18일_축제.getId(),
                    테코대학교_6월_16일_6월_17일_축제.getId(),
                    테코대학교_6월_15일_6월_15일_축제.getId()
                );
        }

        @Test
        void 진행_중_진행_예정_축제는_종료일을_포함하지_않는다() {
            // given
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(_6월_18일));

            // when
            var actual = schoolFestivalsV2QueryService.findPastFestivalsBySchoolId(테코대학교.getId());

            // then
            assertThat(actual)
                .map(SchoolFestivalV1Response::id)
                .containsExactly(
                    테코대학교_6월_16일_6월_17일_축제.getId(),
                    테코대학교_6월_15일_6월_15일_축제.getId()
                );
        }
    }
}
