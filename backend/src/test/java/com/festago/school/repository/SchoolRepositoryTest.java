package com.festago.school.repository;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.support.RepositoryTest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class SchoolRepositoryTest {

    @Autowired
    SchoolRepository schoolRepository;

    @Test
    void 지역으로_학교를_검새한다() {
        // given
        School expectSchool = schoolRepository.save(new School("domain", "name", SchoolRegion.서울));
        schoolRepository.save(new School("domain2", "name2", SchoolRegion.부산));
        schoolRepository.save(new School("domain3", "name3", SchoolRegion.대구));

        // when
        List<School> actual = schoolRepository.findAllByRegion(SchoolRegion.서울);

        // then
        Assertions.assertThat(actual).containsExactly(expectSchool);
    }
}
