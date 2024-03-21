package com.festago.festival.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.application.PopularFestivalV1QueryService;
import com.festago.festival.application.command.FestivalCreateService;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.support.ApplicationIntegrationTest;
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
    FestivalCreateService festivalCreateService;

    @Autowired
    SchoolCommandService schoolCommandService;

    LocalDate now = LocalDate.parse("2077-06-30");

    Long 대학교_식별자;

    Long 첫번째로_저장된_축제_식별자;
    Long 두번째로_저장된_축제_식별자;
    Long 세번째로_저장된_축제_식별자;
    Long 네번째로_저장된_축제_식별자;
    Long 다섯번째로_저장된_축제_식별자;
    Long 여섯번째로_저장된_축제_식별자;
    Long 일곱번째로_저장된_축제_식별자;
    Long 여덟번째로_저장된_축제_식별자;

    @BeforeEach
    void setUp() {
        대학교_식별자 = schoolCommandService.createSchool(new SchoolCreateCommand(
            "테코대학교",
            "teco.ac.kr",
            SchoolRegion.서울,
            "https://image.com/logo.png",
            "https://image.com/background.png"
        ));

        첫번째로_저장된_축제_식별자 = festivalCreateService.createFestival(getFestivalCreateCommand("첫번째로 저장된 축제"));
        두번째로_저장된_축제_식별자 = festivalCreateService.createFestival(getFestivalCreateCommand("두번째로 저장된 축제"));
        세번째로_저장된_축제_식별자 = festivalCreateService.createFestival(getFestivalCreateCommand("세번째로 저장된 축제"));
        네번째로_저장된_축제_식별자 = festivalCreateService.createFestival(getFestivalCreateCommand("네번째로 저장된 축제"));
        다섯번째로_저장된_축제_식별자 = festivalCreateService.createFestival(getFestivalCreateCommand("다섯번째로 저장된 축제"));
        여섯번째로_저장된_축제_식별자 = festivalCreateService.createFestival(getFestivalCreateCommand("여섯번째로 저장된 축제"));
        일곱번째로_저장된_축제_식별자 = festivalCreateService.createFestival(getFestivalCreateCommand("일곱번째로 저장된 축제"));
        여덟번째로_저장된_축제_식별자 = festivalCreateService.createFestival(getFestivalCreateCommand("여덟번째로 저장된 축제"));
    }

    private FestivalCreateCommand getFestivalCreateCommand(String schoolName) {
        return new FestivalCreateCommand(schoolName, now, now, "https://image.com/posterImage.png", 대학교_식별자);
    }

    @Test
    void 인기_축제는_7개까지_반환되고_식별자의_내림차순으로_정렬되어_조회된다() {
        // given && when
        var expect = popularQueryService.findPopularFestivals().content();

        // then
        assertThat(expect)
            .map(FestivalV1Response::id)
            .containsExactly(
                여덟번째로_저장된_축제_식별자,
                일곱번째로_저장된_축제_식별자,
                여섯번째로_저장된_축제_식별자,
                다섯번째로_저장된_축제_식별자,
                네번째로_저장된_축제_식별자,
                세번째로_저장된_축제_식별자,
                두번째로_저장된_축제_식별자
            );
    }
}
