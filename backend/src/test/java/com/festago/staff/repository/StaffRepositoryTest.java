package com.festago.staff.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.staff.domain.Staff;
import com.festago.staff.domain.StaffCode;
import com.festago.support.FestivalFixture;
import com.festago.support.SchoolFixture;
import com.festago.support.StaffFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class StaffRepositoryTest {

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Nested
    class 축제로_존재여부_확인 {

        @Test
        void 있으면_참() {
            // given
            Festival festival = saveFestival();
            staffRepository.save(StaffFixture.staff().codeValue("festa1234").festival(festival).build());

            // when
            boolean result = staffRepository.existsByFestival(festival);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 없으면_거짓() {
            // given
            Festival festival = saveFestival();

            // when
            boolean result = staffRepository.existsByFestival(festival);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class 코드로_존재여부_확인 {

        @Test
        void 있으면_참() {
            // given
            Festival festival = saveFestival();
            String code = "festa1234";
            staffRepository.save(StaffFixture.staff().codeValue(code).festival(festival).build());

            // when
            boolean result = staffRepository.existsByCode(new StaffCode(code));

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 없으면_거짓() {
            // given
            String code = "festa1234";

            // when
            boolean result = staffRepository.existsByCode(new StaffCode(code));

            // then
            assertThat(result).isFalse();
        }
    }

    @Test
    void code로_조회() {
        // given
        String code = "festa1234";
        Festival festival = saveFestival();
        Staff saved = staffRepository.save(
            StaffFixture.staff().codeValue(code).festival(festival).build());

        // when
        Optional<Staff> result = staffRepository.findByCodeWithFetch(code);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isNotEmpty();
            softly.assertThat(result.get().getId()).isEqualTo(saved.getId());
            softly.assertThat(result.get().getFestival()).isEqualTo(festival);
        });
    }

    private Festival saveFestival() {
        School school = schoolRepository.save(SchoolFixture.school().build());
        Festival festival = festivalRepository.save(FestivalFixture.festival().school(school).build());
        return festival;
    }
}
