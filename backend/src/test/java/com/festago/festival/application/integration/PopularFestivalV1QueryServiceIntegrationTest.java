package com.festago.festival.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.application.PopularFestivalV1QueryService;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalInfoSerializer;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

// TODO Repository 사용하지 않고 Service로 데이터 세팅하도록 변경
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
    ArtistRepository artistRepository;

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

    @Autowired
    FestivalInfoSerializer festivalInfoSerializer;

    LocalDate now = LocalDate.parse("2077-06-30");

    Festival 첫번째로_저장된_축제;
    Festival 두번째로_저장된_축제;
    Festival 세번째로_저장된_축제;
    Festival 네번째로_저장된_축제;
    Festival 다섯번째로_저장된_축제;
    Festival 여섯번째로_저장된_축제;
    Festival 일곱번째로_저장된_축제;
    Festival 여덟번째로_저장된_축제;

    @BeforeEach
    void setting() {
        School 테코대학교 = schoolRepository.save(new School("teco.ac.kr", "테코대학교", SchoolRegion.서울));

        첫번째로_저장된_축제 = festivalRepository.save(new Festival("첫번째로 저장된 축제", now, now, 테코대학교));
        두번째로_저장된_축제 = festivalRepository.save(new Festival("두번째로 저장된 축제", now, now, 테코대학교));
        세번째로_저장된_축제 = festivalRepository.save(new Festival("세번째로 저장된 축제", now, now, 테코대학교));
        네번째로_저장된_축제 = festivalRepository.save(new Festival("네번째로 저장된 축제", now, now, 테코대학교));
        다섯번째로_저장된_축제 = festivalRepository.save(new Festival("다섯번째로 저장된 축제", now, now, 테코대학교));
        여섯번째로_저장된_축제 = festivalRepository.save(new Festival("여섯번째로 저장된 축제", now, now, 테코대학교));
        일곱번째로_저장된_축제 = festivalRepository.save(new Festival("일곱번째로 저장된 축제", now, now, 테코대학교));
        여덟번째로_저장된_축제 = festivalRepository.save(new Festival("여덟번째로 저장된 축제", now, now, 테코대학교));

        Artist artist = artistRepository.save(new Artist("name1", "image1"));
        List<Artist> artists = List.of(artist);
        festivalInfoRepository.save(FestivalQueryInfo.of(첫번째로_저장된_축제, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalQueryInfo.of(두번째로_저장된_축제, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalQueryInfo.of(세번째로_저장된_축제, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalQueryInfo.of(네번째로_저장된_축제, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalQueryInfo.of(다섯번째로_저장된_축제, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalQueryInfo.of(여섯번째로_저장된_축제, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalQueryInfo.of(일곱번째로_저장된_축제, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalQueryInfo.of(여덟번째로_저장된_축제, artists, festivalInfoSerializer));
    }

    @Test
    void 인기_축제는_7개까지_반환되고_식별자의_내림차순으로_정렬되어_조회된다() {
        // given && when
        var expect = popularQueryService.findPopularFestivals().content();

        // then
        assertThat(expect)
            .map(FestivalV1Response::id)
            .hasSize(7)
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
