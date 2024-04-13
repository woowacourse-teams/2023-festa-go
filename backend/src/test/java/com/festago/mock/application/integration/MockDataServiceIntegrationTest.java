package com.festago.mock.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.mock.application.MockDataService;
import com.festago.support.ApplicationIntegrationTest;
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
    EntityManager em;

    @Nested
    class makeMockFestivals {

        @BeforeEach
        void setUp() {
            mockDataService.makeMockSchools();
            mockDataService.makeMockArtist();
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
