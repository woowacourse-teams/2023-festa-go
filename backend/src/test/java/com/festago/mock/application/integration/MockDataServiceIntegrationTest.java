package com.festago.mock.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.mock.application.MockDataService;
import com.festago.mock.repository.ForMockSchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.SchoolFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MockDataServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    MockDataService mockDataService;

    @Autowired
    ForMockSchoolRepository schoolRepository;

    @Autowired
    EntityManager em;

    @Nested
    class initialize {

        @Test
        void 만약_하나의_학교라도_존재하면_초기화되지_않는다() {
            // given
            schoolRepository.save(SchoolFixture.builder().build());

            // when
            mockDataService.initialize();
            long schoolCount = schoolRepository.count();

            // then
            assertThat(schoolCount).isEqualTo(1);
        }
    }

    @Nested
    class makeMockFestivals {

        @BeforeEach
        void setUp() {
            mockDataService.initialize();
        }

        @Test
        void 쿼리_최적화_정보가_생성되어야_한다() {
            // given
            mockDataService.makeMockFestivals(7);

            // when
            Long stageQueryInfoCount = em.createQuery("select count(*) from StageQueryInfo sqi", Long.class)
                .getSingleResult();
            Long festivalQueryInfoCount = em.createQuery("select count(*) from FestivalQueryInfo fqi", Long.class)
                .getSingleResult();

            // then
            assertSoftly(softly -> {
                assertThat(stageQueryInfoCount).isNotZero();
                assertThat(festivalQueryInfoCount).isNotZero();
            });
        }
    }
}
