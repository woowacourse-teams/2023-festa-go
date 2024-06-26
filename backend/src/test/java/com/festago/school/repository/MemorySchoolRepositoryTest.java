package com.festago.school.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.support.fixture.SchoolFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemorySchoolRepositoryTest {

    SchoolRepository schoolRepository;

    @BeforeEach
    void setUp() {
        schoolRepository = new MemorySchoolRepository();
    }

    @Test
    void 학교를_저장한다() {
        // given
        School school = schoolRepository.save(SchoolFixture.builder().build());

        // when && then
        assertThat(school.getId()).isPositive();
    }

    @Test
    void 특정_필드로_조회한다() {
        // given
        schoolRepository.save(SchoolFixture.builder()
            .region(SchoolRegion.서울)
            .name("학교이름")
            .domain("knu.ac.kr")
            .build()
        );

        // when && then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(schoolRepository.findByName("학교이름")).isNotEmpty();
            softly.assertThat(schoolRepository.findByName("없는학교")).isEmpty();
            softly.assertThat(schoolRepository.existsByName("학교이름")).isTrue();
            softly.assertThat(schoolRepository.existsByName("없는학교")).isFalse();
            softly.assertThat(schoolRepository.existsByDomain("knu.ac.kr")).isTrue();
            softly.assertThat(schoolRepository.existsByDomain("no.ac.kr")).isFalse();
        });
    }
}
