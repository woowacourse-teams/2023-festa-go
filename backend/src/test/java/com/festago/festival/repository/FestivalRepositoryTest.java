package com.festago.festival.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
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

    @Test
    void 종료_축제는_종료_시점이_최근인_순서로_반환된다() {
        // given
        FestivalFilter filter = FestivalFilter.END;
        Festival festival1 = festivalRepository.save(
            new Festival(PAST_FESTIVAL, now.minusDays(3L), now.minusDays(1L), school));
        Festival festival2 = festivalRepository.save(
            new Festival(CURRENT_FESTIVAL, now, now, school));
        Festival festival3 = festivalRepository.save(
            new Festival(FUTURE_FESTIVAL, now.plusDays(1L), now.plusDays(3L), school));

        LocalDate currentTime = now.plusDays(4L);

        // when
        List<Festival> actual = festivalRepository.findAll(filter.getSpecification(currentTime));

        // then
        assertThat(actual).isEqualTo(List.of(festival3, festival2, festival1));
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
            List<Festival> actual = festivalRepository.findAll(filter.getSpecification(now));

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
            LocalDate currentTime = LocalDate.parse("2023-10-01");
            Festival festival2 = festivalRepository.save(
                new Festival("festival2", currentTime.plusDays(2), currentTime.plusDays(10), school));
            Festival festival3 = festivalRepository.save(
                new Festival("festival3", currentTime.plusDays(3), currentTime.plusDays(10), school));
            Festival festival1 = festivalRepository.save(
                new Festival("festival1", currentTime.plusDays(1), currentTime.plusDays(10), school));

            // when
            List<Festival> actual = festivalRepository.findAll(filter.getSpecification(currentTime));

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
            List<Festival> actual = festivalRepository.findAll(filter.getSpecification(now));

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
            LocalDate currentTime = LocalDate.parse("2023-10-01");
            Festival festival2 = festivalRepository.save(
                new Festival("festival2", currentTime.minusDays(2), currentTime.plusDays(10), school));
            Festival festival3 = festivalRepository.save(
                new Festival("festival3", currentTime.minusDays(1), currentTime.plusDays(10), school));
            Festival festival1 = festivalRepository.save(
                new Festival("festival1", currentTime.minusDays(3), currentTime.plusDays(10), school));

            // when
            List<Festival> actual = festivalRepository.findAll(filter.getSpecification(currentTime));

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
            List<Festival> actual = festivalRepository.findAll(filter.getSpecification(now));

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual).hasSize(1);
                assertThat(actual.get(0)).matches(festival -> festival.getName().equals(PAST_FESTIVAL));
            });
        }

        @Test
        void 은_시작_시점이_빠른_순서로_반환된다() {
            // given
            FestivalFilter filter = FestivalFilter.END;
            LocalDate currentTime = LocalDate.parse("2023-10-01");
            Festival festival2 = festivalRepository.save(
                new Festival("festival2", currentTime.minusDays(10), currentTime.minusDays(2), school));
            Festival festival3 = festivalRepository.save(
                new Festival("festival3", currentTime.minusDays(10), currentTime.minusDays(1), school));
            Festival festival1 = festivalRepository.save(
                new Festival("festival1", currentTime.minusDays(10), currentTime.minusDays(3), school));

            // when
            List<Festival> actual = festivalRepository.findAll(filter.getSpecification(currentTime));

            // then
            assertThat(actual).isEqualTo(List.of(festival3, festival2, festival1));
        }
    }
}
