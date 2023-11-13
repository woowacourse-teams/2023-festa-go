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

    @BeforeEach
    public void setting() {
        School school = schoolRepository.save(new School("domain", "name"));
        festivalRepository.save(new Festival(PAST_FESTIVAL, now.minusDays(3L), now.minusDays(1L), school));
        festivalRepository.save(new Festival(CURRENT_FESTIVAL, now, now, school));
        festivalRepository.save(new Festival(FUTURE_FESTIVAL, now.plusDays(1L), now.plusDays(3L), school));
    }

    @Test
    void 진행_예정_축제_반환() {
        // given
        FestivalFilter filter = FestivalFilter.PLANNED;

        // when
        List<Festival> actual = festivalRepository.findAll(filter.getSpecification(now));

        // then
        assertSoftly(softAssertions -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0)).matches(festival -> festival.getName().equals(FUTURE_FESTIVAL));
        });
    }

    @Test
    void 진행_중_축제_반환() {
        // given
        FestivalFilter filter = FestivalFilter.PROGRESS;

        // when
        List<Festival> actual = festivalRepository.findAll(filter.getSpecification(now));

        // then
        assertSoftly(softAssertions -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0)).matches(festival -> festival.getName().equals(CURRENT_FESTIVAL));
        });
    }

    @Test
    void 종료_축제_반환() {
        // given
        FestivalFilter filter = FestivalFilter.END;

        // when
        List<Festival> actual = festivalRepository.findAll(filter.getSpecification(now));

        // then
        assertSoftly(softAssertions -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0)).matches(festival -> festival.getName().equals(PAST_FESTIVAL));
        });
    }
}
