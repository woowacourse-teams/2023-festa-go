package com.festago.festival.application.integration.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.application.PopularFestivalV1QueryService;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.FestivalQueryInfoFixture;
import com.festago.support.fixture.SchoolFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PopularFestivalV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    PopularFestivalV1QueryService popularQueryService;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

    @Autowired
    SchoolRepository schoolRepository;

    School 대학교;

    Festival 첫번째로_저장된_축제;
    Festival 두번째로_저장된_축제;
    Festival 세번째로_저장된_축제;
    Festival 네번째로_저장된_축제;
    Festival 다섯번째로_저장된_축제;
    Festival 여섯번째로_저장된_축제;
    Festival 일곱번째로_저장된_축제;
    Festival 여덟번째로_저장된_축제;

    @BeforeEach
    void setUp() {
        대학교 = schoolRepository.save(SchoolFixture.builder().build());

        첫번째로_저장된_축제 = createFestival();
        두번째로_저장된_축제 = createFestival();
        세번째로_저장된_축제 = createFestival();
        네번째로_저장된_축제 = createFestival();
        다섯번째로_저장된_축제 = createFestival();
        여섯번째로_저장된_축제 = createFestival();
        일곱번째로_저장된_축제 = createFestival();
        여덟번째로_저장된_축제 = createFestival();
    }

    private Festival createFestival() {
        Festival festival = festivalRepository.save(FestivalFixture.builder().school(대학교).build());
        festivalInfoRepository.save(FestivalQueryInfoFixture.builder().festivalId(festival.getId()).build());
        return festival;
    }

    @Test
    void 인기_축제는_7개까지_반환되고_식별자의_내림차순으로_정렬되어_조회된다() {
        // when
        var expect = popularQueryService.findPopularFestivals().content();

        // then
        assertThat(expect)
            .map(FestivalV1Response::id)
            .containsExactly(
                여덟번째로_저장된_축제.getId(),
                일곱번째로_저장된_축제.getId(),
                여섯번째로_저장된_축제.getId(),
                다섯번째로_저장된_축제.getId(),
                네번째로_저장된_축제.getId(),
                세번째로_저장된_축제.getId(),
                두번째로_저장된_축제.getId()
            );
    }
}
