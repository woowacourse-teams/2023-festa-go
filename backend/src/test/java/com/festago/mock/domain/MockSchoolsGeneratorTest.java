package com.festago.mock.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MockSchoolsGeneratorTest {

    MockSchoolsGenerator mockSchoolsGenerator = new MockSchoolsGenerator();

    @Test
    void SchoolRegion_ANY를_제외한_지역_당_3개의_학교를_생성한다() {
        // when
        List<School> actual = mockSchoolsGenerator.generate();

        // then
        int expectSize = (SchoolRegion.values().length - 1) * 3;
        assertSoftly(softly -> {
            softly.assertThat(actual)
                .map(School::getRegion)
                .doesNotContain(SchoolRegion.ANY);
            softly.assertThat(actual).hasSize(expectSize);
        });
    }
}
