package com.festago.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminStageIdResolverQueryDslRepositoryTest extends ApplicationIntegrationTest {

    @Autowired
    AdminStageIdResolverQueryDslRepository adminStageIdResolverQueryDslRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    SchoolRepository schoolRepository;

    School 테코대학교;

    @BeforeEach
    void setUp() {
        테코대학교 = schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());
    }

    @Nested
    class findStageIdsByFestivalId {

        @Test
        void 축제_식별자로_공연의_식별자를_모두_조회한다() {
            // given
            Festival festival = festivalRepository.save(FestivalFixture.builder().school(테코대학교).build());
            List<Long> expect = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> stageRepository.save(StageFixture.builder().festival(festival).build()))
                .map(Stage::getId)
                .toList();

            // when
            List<Long> actual = adminStageIdResolverQueryDslRepository.findStageIdsByFestivalId(festival.getId());

            // then
            assertThat(actual).containsExactlyInAnyOrderElementsOf(expect);
        }
    }

    @Nested
    class findStageIdsByFestivalIdIn {

        @Test
        void 축제_식별자_목록으로_공연의_식별자를_모두_조회한다() {
            // given
            List<Festival> festivals = IntStream.rangeClosed(1, 2)
                .mapToObj(i -> festivalRepository.save(FestivalFixture.builder().school(테코대학교).build()))
                .toList();
            List<Long> expect = festivals.stream()
                .map(festival -> IntStream.rangeClosed(1, 3)
                    .mapToObj(j -> stageRepository.save(StageFixture.builder().festival(festival).build()))
                    .map(Stage::getId)
                    .toList())
                .flatMap(List::stream)
                .toList();

            // when
            List<Long> festivalIds = festivals.stream()
                .map(Festival::getId)
                .toList();
            List<Long> actual = adminStageIdResolverQueryDslRepository.findStageIdsByFestivalIdIn(festivalIds);

            // then
            assertThat(actual).containsExactlyInAnyOrderElementsOf(expect);
        }
    }
}
