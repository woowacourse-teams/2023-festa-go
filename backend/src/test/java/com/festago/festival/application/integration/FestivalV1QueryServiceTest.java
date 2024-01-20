package com.festago.festival.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.application.FestivalV1QueryService;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalInfo;
import com.festago.festival.domain.FestivalInfoConverter;
import com.festago.festival.dto.v1.FestivaV1lListRequest;
import com.festago.festival.dto.v1.FestivalV1ListResponse;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
class FestivalV1QueryServiceTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    FestivalInfoConverter festivalInfoConverter;

    @Autowired
    FestivalV1QueryService festivalV1QueryService;

    @BeforeEach
    void setting() {
        LocalDate now = LocalDate.now();

        School school1 = schoolRepository.save(new School("domain1", "school1", SchoolRegion.서울));
        School school2 = schoolRepository.save(new School("domain2", "school2", SchoolRegion.서울));
        School school3 = schoolRepository.save(new School("domain3", "school3", SchoolRegion.대구));

        Festival festival1 = festivalRepository.save(new Festival("festival1", now, now.plusDays(2), school1));
        Festival festival2 = festivalRepository.save(
            new Festival("festival2", now, now.plusDays(3), school2));
        Festival festival3 = festivalRepository.save(
            new Festival("festival3", now.plusDays(2), now.plusDays(4), school3));

        Artist artist1 = artistRepository.save(new Artist("name1", "image1"));
        Artist artist2 = artistRepository.save(new Artist("name2", "image2"));
        Artist artist3 = artistRepository.save(new Artist("name3", "image3"));

        List<Artist> artists = List.of(artist1, artist2, artist3);

        FestivalInfo festivalInfo1 = festivalInfoRepository.save(
            FestivalInfo.of(festival1, artists, festivalInfoConverter));
        FestivalInfo festivalInfo2 = festivalInfoRepository.save(
            FestivalInfo.of(festival2, artists, festivalInfoConverter));
        FestivalInfo festivalInfo3 = festivalInfoRepository.save(
            FestivalInfo.of(festival3, artists, festivalInfoConverter));
    }

    @Nested
    class 지역_없는_검색에서 {

        @Test
        void 마지막_페이지_없이_검색하면_진행_중인_모든_축제를_반환한다() {
            // given
            FestivaV1lListRequest request = new FestivaV1lListRequest(null, null, null, null, null);

            // when
            FestivalV1ListResponse actual = festivalV1QueryService.findFestivals(request);

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual.isLastPage()).isTrue();
                assertThat(actual.festivals()).hasSize(2);
            });
        }

        @Test
        void 마지막_페이지가_아닌지_반환받을_수_있다() {
            // given
            FestivaV1lListRequest firstRequest = new FestivaV1lListRequest(null, null, 1, null, null);
            FestivalV1ListResponse firstResponse = festivalV1QueryService.findFestivals(firstRequest);

            Long lastFestivalId = firstResponse.festivals().get(0).id();
            LocalDate lastStartDate = firstResponse.festivals().get(0).startDate();
            FestivaV1lListRequest secondRequest = new FestivaV1lListRequest(null, null, 1, lastFestivalId,
                lastStartDate.toString());

            // when
            FestivalV1ListResponse secondResponse = festivalV1QueryService.findFestivals(secondRequest);

            System.out.println(secondResponse);
            // then
            assertSoftly(softAssertions -> {
                assertThat(firstResponse.isLastPage()).isFalse();
                assertThat(secondResponse.isLastPage()).isTrue();
            });
        }
    }
}
