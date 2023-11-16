package com.festago.festival.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.RepositoryTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class FestivalRepositoryTest {

    private static final String CURRENT_FESTIVAL = "현재 축제";
    private static final String PAST_FESTIVAL = "과거 축제";
    private static final String FUTURE_FESTIVAL = "미래 축제";

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    LocalDate now = LocalDate.parse("2023-06-30");

    School school;

    @BeforeEach
    public void setting() {
        this.school = schoolRepository.save(new School("domain", "name"));
    }

    private void prepareNotOrderedFestivals() {
        festivalRepository.save(new Festival(FUTURE_FESTIVAL, now.plusDays(1L), now.plusDays(3L), school));
        festivalRepository.save(new Festival(CURRENT_FESTIVAL, now, now, school));
        festivalRepository.save(new Festival(PAST_FESTIVAL, now.minusDays(3L), now.minusDays(1L), school));
    }

    @Nested
    class 진행_에정_축제_반환 {

        @Test
        void 성공() {
            // given
            FestivalFilter filter = FestivalFilter.PLANNED;
            prepareNotOrderedFestivals();

            // when
            List<Festival> actual = festivalRepository.findByFilter(filter, now);

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual).hasSize(1);
                assertThat(actual.get(0)).matches(festival -> festival.getName().equals(FUTURE_FESTIVAL));
            });
        }

        @Test
        void 은_시작_시점이_빠른_순서로_반환된다() {
            // given
            FestivalFilter filter = FestivalFilter.PLANNED;
            Festival festival2 = festivalRepository.save(
                new Festival("festival2", now.plusDays(2), now.plusDays(10), school));
            Festival festival3 = festivalRepository.save(
                new Festival("festival3", now.plusDays(3), now.plusDays(10), school));
            Festival festival1 = festivalRepository.save(
                new Festival("festival1", now.plusDays(1), now.plusDays(10), school));

            // when
            List<Festival> actual = festivalRepository.findByFilter(filter, now);

            // then
            assertThat(actual).isEqualTo(List.of(festival1, festival2, festival3));
        }
    }

    @Nested
    class 진행_축제_반환 {

        @Test
        void 성공() {
            // given
            FestivalFilter filter = FestivalFilter.PROGRESS;
            prepareNotOrderedFestivals();

            // when
            List<Festival> actual = festivalRepository.findByFilter(filter, now);

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual).hasSize(1);
                assertThat(actual.get(0)).matches(festival -> festival.getName().equals(CURRENT_FESTIVAL));
            });
        }

        @Test
        void 은_시작_시점이_빠른_순서로_반환된다() {
            // given
            FestivalFilter filter = FestivalFilter.PROGRESS;
            Festival festival2 = festivalRepository.save(
                new Festival("festival2", now.minusDays(2), now.plusDays(10), school));
            Festival festival3 = festivalRepository.save(
                new Festival("festival3", now.minusDays(1), now.plusDays(10), school));
            Festival festival1 = festivalRepository.save(
                new Festival("festival1", now.minusDays(3), now.plusDays(10), school));

            // when
            List<Festival> actual = festivalRepository.findByFilter(filter, now);

            // then
            assertThat(actual).isEqualTo(List.of(festival1, festival2, festival3));
        }
    }

    @Nested
    class 종료_축제_반환 {

        @Test
        void 성공() {
            // given
            FestivalFilter filter = FestivalFilter.END;
            prepareNotOrderedFestivals();

            // when
            List<Festival> actual = festivalRepository.findByFilter(filter, now);

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual).hasSize(1);
                assertThat(actual.get(0)).matches(festival -> festival.getName().equals(PAST_FESTIVAL));
            });
        }

        @Test
        void 은_종료_시점이_느린_순서로_반환된다() {
            // given
            FestivalFilter filter = FestivalFilter.END;
            Festival festival2 = festivalRepository.save(
                new Festival("festival2", now.minusDays(10), now.minusDays(2), school));
            Festival festival3 = festivalRepository.save(
                new Festival("festival3", now.minusDays(10), now.minusDays(1), school));
            Festival festival1 = festivalRepository.save(
                new Festival("festival1", now.minusDays(10), now.minusDays(3), school));

            // when
            List<Festival> actual = festivalRepository.findByFilter(filter, now);

            // then
            assertThat(actual).isEqualTo(List.of(festival3, festival2, festival1));
        }
    }
}
