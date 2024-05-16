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
import java.time.LocalDate;
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
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

    School 대학교;

    Festival 첫번째로_저장된_축제;
    Festival 두번째로_저장된_축제;
    Festival 세번째로_저장된_축제;
    Festival 네번째로_저장된_축제;
    Festival 다섯번째로_저장된_축제;
    Festival 여섯번째로_저장된_축제;
    Festival 일곱번째로_저장된_축제;
    Festival 여덟번째로_저장된_축제;
    Festival 아홉번째로_저장된_공연없는_축제;
    Festival 열번째로_저장된_공연없는_축제;
    Festival 열한번쨰로_저장된_기간이_지난_축제;

    @BeforeEach
    void setUp() {
        대학교 = schoolRepository.save(SchoolFixture.builder().build());

        LocalDate startDate = LocalDate.now();;
        LocalDate endDate = startDate.plusDays(3);

        첫번째로_저장된_축제 = createFestivalWithFilledFestivalInfo(startDate, endDate);
        두번째로_저장된_축제 = createFestivalWithFilledFestivalInfo(startDate, endDate);
        세번째로_저장된_축제 = createFestivalWithFilledFestivalInfo(startDate, endDate);
        네번째로_저장된_축제 = createFestivalWithFilledFestivalInfo(startDate, endDate);
        다섯번째로_저장된_축제 = createFestivalWithFilledFestivalInfo(startDate, endDate);
        여섯번째로_저장된_축제 = createFestivalWithFilledFestivalInfo(startDate, endDate);
        일곱번째로_저장된_축제 = createFestivalWithFilledFestivalInfo(startDate, endDate);
        여덟번째로_저장된_축제 = createFestivalWithFilledFestivalInfo(startDate, endDate);
        아홉번째로_저장된_공연없는_축제 = createFestivalWithEmptyFestivalInfo(startDate, endDate);
        열번째로_저장된_공연없는_축제 = createFestivalWithEmptyFestivalInfo(startDate, endDate);
        열한번쨰로_저장된_기간이_지난_축제 = createFestivalWithFilledFestivalInfo(startDate.minusDays(10L), endDate.minusDays(13L));
    }

    private Festival createFestivalWithFilledFestivalInfo(LocalDate startDate, LocalDate endDate) {
        Festival festival = festivalRepository.save(makeBaseFestivalFixture(startDate, endDate));
        festivalInfoRepository.save(makeBaseFestivalInfoFixture(festival)
            .artistInfo("""
                {
                    notEmpty
                }
                """)
            .build());
        return festival;
    }

    private Festival makeBaseFestivalFixture(LocalDate startDate, LocalDate endDate) {
        return FestivalFixture.builder()
            .school(대학교)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    private FestivalQueryInfoFixture makeBaseFestivalInfoFixture(Festival festival) {
        return FestivalQueryInfoFixture.builder()
            .festivalId(festival.getId());
    }

    private Festival createFestivalWithEmptyFestivalInfo(LocalDate startDate, LocalDate endDate) {
        Festival festival = festivalRepository.save(makeBaseFestivalFixture(startDate, endDate));
        festivalInfoRepository.save(makeBaseFestivalInfoFixture(festival)
            .build());
        return festival;
    }

    @Test
    void 인기_축제는_공연이등록된_축제중_7개까지_반환되고_식별자의_내림차순으로_정렬되어_조회된다() {
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

    @Test
    void 축제_기간이_끝난_축제는_반환되지_않는다() {
        // when
        var expect = popularQueryService.findPopularFestivals().content();

        // then
        assertThat(expect)
            .map(FestivalV1Response::id)
            .doesNotContain(
                열한번쨰로_저장된_기간이_지난_축제.getId()
            );
    }
}
