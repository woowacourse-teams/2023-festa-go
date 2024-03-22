package com.festago.school.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.school.application.v1.SchoolSearchV1QueryService;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.SchoolSearchV1Response;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.SchoolFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SchoolSearchV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolSearchV1QueryService schoolSearchV1QueryService;

    @Autowired
    SchoolRepository schoolRepository;

    School 테코대학교;

    School 테코여자대학교;

    School 우테대학교;

    @BeforeEach
    void setUp() {
        테코대학교 = schoolRepository.save(SchoolFixture.school().name("테코대학교").domain("teco.ac.kr").build());
        테코여자대학교 = schoolRepository.save(SchoolFixture.school().name("테코여자대학교").domain("tecowoman.ac.kr").build());
        우테대학교 = schoolRepository.save(SchoolFixture.school().name("우테대학교").domain("woote.ac.kr").build());
    }

    @Test
    void 우테대학교를_검색하면_우테대학교가_검색되어야_한다() {
        // given
        String keyword = "우테대학교";

        // when
        var response = schoolSearchV1QueryService.searchSchools(keyword);

        // then
        assertThat(response)
            .map(SchoolSearchV1Response::id)
            .containsExactly(우테대학교.getId());
    }

    @Test
    void 테코를_검색하면_테코대학교와_테코여자대학교가_검색되어야_한다() {
        // given
        String keyword = "테코";

        // when
        var response = schoolSearchV1QueryService.searchSchools(keyword);

        // then
        assertThat(response)
            .map(SchoolSearchV1Response::id)
            .containsExactly(테코대학교.getId(), 테코여자대학교.getId());
    }

    @Test
    void 학교의_이름에_포함되지_않으면_빈_리스트가_반환된다() {
        // given
        String keyword = "글렌";

        // when
        var response = schoolSearchV1QueryService.searchSchools(keyword);

        // then
        assertThat(response).isEmpty();
    }
}
