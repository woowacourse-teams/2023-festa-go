package com.festago.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminFestivalIdResolverQueryDslRepositoryTest extends ApplicationIntegrationTest {

    @Autowired
    AdminFestivalIdResolverQueryDslRepository adminFestivalIdResolverQueryDslRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    School 테코대학교;

    LocalDate _6월_12일 = LocalDate.parse("2077-06-12");
    LocalDate _6월_13일 = LocalDate.parse("2077-06-13");
    LocalDate _6월_14일 = LocalDate.parse("2077-06-14");
    LocalDate _6월_15일 = LocalDate.parse("2077-06-15");

    @BeforeEach
    void setUp() {
        테코대학교 = schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());
    }

    @Nested
    class findFestivalIdsByWithinDates {

        @Test
        void 축제의_시작일에_포함되는_축제의_식별자_목록을_반환한다() {
            // given
            Festival _6월_12일_축제 = festivalRepository.save(
                FestivalFixture.builder().startDate(_6월_12일).school(테코대학교).build());
            Festival _6월_13일_축제 = festivalRepository.save(
                FestivalFixture.builder().startDate(_6월_13일).school(테코대학교).build());
            Festival _6월_14일_축제 = festivalRepository.save(
                FestivalFixture.builder().startDate(_6월_14일).school(테코대학교).build());
            Festival _6월_15일_축제 = festivalRepository.save(
                FestivalFixture.builder().startDate(_6월_15일).school(테코대학교).build());

            // when
            var actual = adminFestivalIdResolverQueryDslRepository.findFestivalIdsByStartDatePeriod(_6월_13일, _6월_14일);

            // then
            assertThat(actual).containsExactlyInAnyOrder(_6월_13일_축제.getId(), _6월_14일_축제.getId());
        }
    }
}
