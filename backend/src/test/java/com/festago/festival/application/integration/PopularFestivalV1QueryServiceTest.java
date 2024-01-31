package com.festago.festival.application.integration;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.application.PopularFestivalV1QueryService;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalInfoSerializer;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.dto.PopularFestivalsV1Response;
import com.festago.festival.repository.FestivalQueryInfoRepository;
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

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PopularFestivalV1QueryServiceTest extends ApplicationIntegrationTest {

    @Autowired
    PopularFestivalV1QueryService popularQueryService;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    FestivalQueryInfoRepository festivalQueryInfoRepository;

    @Autowired
    FestivalInfoSerializer festivalInfoSerializer;

    @BeforeEach
    void setting() {
        LocalDate now = LocalDate.now();

        School school1 = schoolRepository.save(new School("domain1", "school1", SchoolRegion.서울));
        School school2 = schoolRepository.save(new School("domain2", "school2", SchoolRegion.서울));
        School school3 = schoolRepository.save(new School("domain3", "school3", SchoolRegion.대구));

        Festival festival1 = festivalRepository.save(
            new Festival("festival1", now.minusDays(4), now.plusDays(2), school1));
        Festival festival2 = festivalRepository.save(
            new Festival("festival2", now.minusDays(4), now.plusDays(3), school2));
        Festival festival3 = festivalRepository.save(
            new Festival("festival3", now.plusDays(3), now.plusDays(4), school3));
        Festival festival4 = festivalRepository.save(
            new Festival("festival1", now.minusDays(3), now.plusDays(2), school1));
        Festival festival5 = festivalRepository.save(
            new Festival("festival1", now.minusDays(2), now.plusDays(2), school1));
        Festival festival6 = festivalRepository.save(
            new Festival("festival1", now.minusDays(1), now.plusDays(2), school1));
        Festival festival7 = festivalRepository.save(
            new Festival("festival3", now.plusDays(3), now.plusDays(4), school3));
        Festival festival8 = festivalRepository.save(
            new Festival("festival3", now.plusDays(2), now.plusDays(4), school3));

        Artist artist1 = artistRepository.save(new Artist("name1", "image1"));
        Artist artist2 = artistRepository.save(new Artist("name2", "image2"));
        Artist artist3 = artistRepository.save(new Artist("name3", "image3"));

        List<Artist> artists = List.of(artist1, artist2, artist3);

        festivalQueryInfoRepository.save(FestivalQueryInfo.of(festival1, artists, festivalInfoSerializer));
        festivalQueryInfoRepository.save(FestivalQueryInfo.of(festival2, artists, festivalInfoSerializer));
        festivalQueryInfoRepository.save(FestivalQueryInfo.of(festival3, artists, festivalInfoSerializer));
        festivalQueryInfoRepository.save(FestivalQueryInfo.of(festival4, artists, festivalInfoSerializer));
        festivalQueryInfoRepository.save(FestivalQueryInfo.of(festival5, artists, festivalInfoSerializer));
        festivalQueryInfoRepository.save(FestivalQueryInfo.of(festival6, artists, festivalInfoSerializer));
        festivalQueryInfoRepository.save(FestivalQueryInfo.of(festival7, artists, festivalInfoSerializer));
        festivalQueryInfoRepository.save(FestivalQueryInfo.of(festival8, artists, festivalInfoSerializer));
    }

    @Test
    void 인기_축제_목록을_받는다() {
        // given && when
        PopularFestivalsV1Response actual = popularQueryService.findPopularFestivals();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.title()).isEqualTo("요즘 뜨는 축제");
            softly.assertThat(actual.content()).hasSize(7);
        });
    }

}
