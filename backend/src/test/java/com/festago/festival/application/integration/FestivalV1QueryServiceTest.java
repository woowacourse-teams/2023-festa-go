package com.festago.festival.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.application.FestivalV1QueryService;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalInfo;
import com.festago.festival.domain.FestivalInfoSerializer;
import com.festago.festival.dto.v1.FestivalV1ListResponse;
import com.festago.festival.dto.v1.FestivalV1Response;
import com.festago.festival.dto.v1.FestivalV1lListRequest;
import com.festago.festival.repository.FestivalFilter;
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
    FestivalInfoSerializer festivalInfoSerializer;
    @Autowired
    FestivalV1QueryService festivalV1QueryService;
    @Autowired
    ObjectMapper objectMapper;

    private long firstPlannedFestivalId = 0;

    @BeforeEach
    void setting() {
        LocalDate now = LocalDate.now();

        School school1 = schoolRepository.save(new School("domain1", "school1", SchoolRegion.서울));
        School school2 = schoolRepository.save(new School("domain2", "school2", SchoolRegion.서울));
        School school3 = schoolRepository.save(new School("domain3", "school3", SchoolRegion.대구));

        Festival progressFestival1 = festivalRepository.save(
            new Festival("festival1", now.minusDays(4), now.plusDays(2), school1));
        Festival progressFestival2 = festivalRepository.save(
            new Festival("festival2", now.minusDays(4), now.plusDays(3), school2));
        Festival plannedFestival1 = festivalRepository.save(
            new Festival("festival3", now.plusDays(3), now.plusDays(4), school3));

        Festival progressFestival3 = festivalRepository.save(
            new Festival("festival1", now.minusDays(3), now.plusDays(2), school1));
        Festival progressFestival4 = festivalRepository.save(
            new Festival("festival1", now.minusDays(2), now.plusDays(2), school1));
        Festival progressFestival5 = festivalRepository.save(
            new Festival("festival1", now.minusDays(1), now.plusDays(2), school1));

        Festival plannedFestival2 = festivalRepository.save(
            new Festival("festival3", now.plusDays(3), now.plusDays(4), school3));
        Festival plannedFestival3 = festivalRepository.save(
            new Festival("festival3", now.plusDays(2), now.plusDays(4), school3));
        firstPlannedFestivalId = plannedFestival3.getId();

        Artist artist1 = artistRepository.save(new Artist("name1", "image1"));
        Artist artist2 = artistRepository.save(new Artist("name2", "image2"));
        Artist artist3 = artistRepository.save(new Artist("name3", "image3"));

        List<Artist> artists = List.of(artist1, artist2, artist3);

        festivalInfoRepository.save(FestivalInfo.of(progressFestival1, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalInfo.of(progressFestival2, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalInfo.of(progressFestival3, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalInfo.of(progressFestival4, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalInfo.of(progressFestival5, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalInfo.of(plannedFestival1, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalInfo.of(plannedFestival2, artists, festivalInfoSerializer));
        festivalInfoRepository.save(FestivalInfo.of(plannedFestival3, artists, festivalInfoSerializer));
    }

    @Nested
    class 지역_없는_검색에서 {

        @Test
        void 마지막_페이지_없이_검색하면_진행_중인_모든_축제를_반환한다() {
            // given
            FestivalV1lListRequest request = new FestivalV1lListRequest(null, null, null, null, null);

            // when
            FestivalV1ListResponse actual = festivalV1QueryService.findFestivals(request);

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual.isLastPage()).isTrue();
                assertThat(actual.festivals()).hasSize(5);
            });
        }

        @Test
        void 마지막_페이지가_아닌지_반환받을_수_있다() {
            // given
            FestivalV1lListRequest firstRequest = new FestivalV1lListRequest(null, null, 1, null, null);
            FestivalV1ListResponse firstResponse = festivalV1QueryService.findFestivals(firstRequest);

            Long lastFestivalId = firstResponse.festivals().get(0).id();
            LocalDate lastStartDate = firstResponse.festivals().get(0).startDate();
            FestivalV1lListRequest secondRequest = new FestivalV1lListRequest(null, null, 4, lastFestivalId,
                lastStartDate.toString());

            // when
            FestivalV1ListResponse secondResponse = festivalV1QueryService.findFestivals(secondRequest);

            // then
            assertSoftly(softAssertions -> {
                assertThat(firstResponse.isLastPage()).isFalse();
                assertThat(secondResponse.isLastPage()).isTrue();
            });
        }

        @Test
        void 진행_에정_필터를_적용할_수_있다() {
            // given
            FestivalV1lListRequest request = new FestivalV1lListRequest(null, FestivalFilter.PLANNED.name(), 10, null,
                null);

            // when
            FestivalV1ListResponse response = festivalV1QueryService.findFestivals(request);

            // then
            assertSoftly(softAssertions -> {
                assertThat(response.isLastPage()).isTrue();
                assertThat(response.festivals()).hasSize(3);
                assertThat(response.festivals().get(0).id()).isEqualTo(firstPlannedFestivalId);
            });
        }

        @Test
        void 같은_날짜에_진행_예정인_축제가_있으면_축제_아이디가_빠른_순으로_반환된다() {
            // given
            FestivalV1lListRequest request = new FestivalV1lListRequest(null, FestivalFilter.PLANNED.name(), 10, null,
                null);

            // when
            FestivalV1ListResponse response = festivalV1QueryService.findFestivals(request);
            System.out.println(response.isLastPage());
            System.out.println(response);

            // then
            assertSoftly(softAssertions -> {
                assertThat(response.isLastPage()).isTrue();
                assertThat(response.festivals()).hasSize(3);
                assertThat(
                    response.festivals().get(1).startDate().isEqual(response.festivals().get(2).startDate())).isTrue();
                assertThat(response.festivals().get(1).id()).isLessThan(response.festivals().get(2).id());
            });
        }

        @Test
        void 진행_예정은_날짜가_시작_날짜가_빠른_순으로_반환된다() {
            // given
            FestivalV1lListRequest fistRequest = new FestivalV1lListRequest(SchoolRegion.대구.name(),
                FestivalFilter.PLANNED.name(), 1,
                null, null);
            FestivalV1ListResponse firstResponse = festivalV1QueryService.findFestivals(fistRequest);

            FestivalV1Response firstFestival = firstResponse.festivals().get(0);
            FestivalV1lListRequest secondRequest = new FestivalV1lListRequest(SchoolRegion.대구.name(),
                FestivalFilter.PLANNED.name(), 1,
                firstFestival.id(), firstFestival.startDate().toString());
            FestivalV1ListResponse secondResponse = festivalV1QueryService.findFestivals(secondRequest);

            FestivalV1Response secondFestival = secondResponse.festivals().get(0);
            FestivalV1lListRequest thirdRequest = new FestivalV1lListRequest(SchoolRegion.대구.name(),
                FestivalFilter.PLANNED.name(), 1,
                secondFestival.id(), secondFestival.startDate().toString());
            FestivalV1ListResponse thirdResponse = festivalV1QueryService.findFestivals(thirdRequest);

            FestivalV1Response thirdFestival = thirdResponse.festivals().get(0);

            // when && then
            assertSoftly(softAssertions -> {
                assertThat(firstFestival.startDate()).isBeforeOrEqualTo(secondFestival.startDate());
                assertThat(secondFestival.startDate()).isBeforeOrEqualTo(thirdFestival.startDate());
                assertThat(secondFestival.id()).isLessThan(thirdFestival.id());
            });
        }

        @Test
        void 진행_중인_축제는_시작_날짜가_느린_순으로_반환된다() {
            // given
            FestivalV1lListRequest fistRequest = new FestivalV1lListRequest(null, FestivalFilter.PROGRESS.name(), 1,
                null, null);
            FestivalV1ListResponse firstResponse = festivalV1QueryService.findFestivals(fistRequest);
            FestivalV1Response firstFestival = firstResponse.festivals().get(0);

            FestivalV1lListRequest secondRequest = new FestivalV1lListRequest(null, FestivalFilter.PROGRESS.name(), 1,
                firstFestival.id(), firstFestival.startDate().toString());
            FestivalV1ListResponse secondResponse = festivalV1QueryService.findFestivals(secondRequest);
            FestivalV1Response secondFestival = secondResponse.festivals().get(0);

            FestivalV1lListRequest thirdRequest = new FestivalV1lListRequest(null, FestivalFilter.PROGRESS.name(), 1,
                secondFestival.id(), secondFestival.startDate().toString());
            FestivalV1ListResponse thirdResponse = festivalV1QueryService.findFestivals(thirdRequest);
            FestivalV1Response thirdFestival = thirdResponse.festivals().get(0);

            FestivalV1lListRequest fourthRequest = new FestivalV1lListRequest(null, FestivalFilter.PROGRESS.name(), 1,
                thirdFestival.id(), thirdFestival.startDate().toString());
            FestivalV1ListResponse fourthResponse = festivalV1QueryService.findFestivals(fourthRequest);
            FestivalV1Response fourthFestival = fourthResponse.festivals().get(0);

            // when && then
            assertSoftly(softAssertions -> {
                assertThat(firstFestival.startDate()).isAfter(secondFestival.startDate());
                assertThat(secondFestival.startDate()).isAfter(thirdFestival.startDate());
                assertThat(thirdFestival.startDate()).isAfter(fourthFestival.startDate());
            });
        }

        @Test
        void 진행_중인_축제에_같은_날짜가_있으면_id가_작은_것_부터_보낸다() {
            // given
            FestivalV1lListRequest fistRequest = new FestivalV1lListRequest(null, FestivalFilter.PROGRESS.name(), 4,
                null, null);
            FestivalV1ListResponse firstResponse = festivalV1QueryService.findFestivals(fistRequest);
            FestivalV1Response firstLastFestival = firstResponse.festivals().get(3);

            FestivalV1lListRequest secondRequest = new FestivalV1lListRequest(null, FestivalFilter.PROGRESS.name(), 1,
                firstLastFestival.id(), firstLastFestival.startDate().toString());
            FestivalV1ListResponse secondResponse = festivalV1QueryService.findFestivals(secondRequest);
            FestivalV1Response secondLastFestival = secondResponse.festivals().get(0);

            // when && then
            assertSoftly(softAssertions -> {
                assertThat(firstLastFestival.startDate()).isEqualTo(secondLastFestival.startDate());
                assertThat(firstLastFestival.id()).isLessThan(secondLastFestival.id());
                assertThat(secondResponse.isLastPage()).isTrue();
            });
        }
    }

    @Nested
    class 지역_검색_에서 {

        @Test
        void 지역을_선택할_수_있다() {
            // given
            FestivalV1lListRequest firstRequest = new FestivalV1lListRequest(SchoolRegion.대구.name(),
                FestivalFilter.PLANNED.name(), 10, null, null);
            FestivalV1lListRequest secondRequest = new FestivalV1lListRequest(SchoolRegion.서울.name(),
                FestivalFilter.PLANNED.name(), 10, null, null);

            // when
            FestivalV1ListResponse firstResponse = festivalV1QueryService.findFestivals(firstRequest);
            FestivalV1ListResponse secondResponse = festivalV1QueryService.findFestivals(secondRequest);

            // then
            assertSoftly(softAssertions -> {
                assertThat(firstResponse.isLastPage()).isTrue();
                assertThat(firstResponse.festivals()).hasSize(3);
                assertThat(secondResponse.isLastPage()).isTrue();
                assertThat(secondResponse.festivals()).hasSize(0);
            });
        }

        @Test
        void 같은_날짜에_진행_예정인_축제가_있으면_축제_아이디가_빠른_순으로_반환된다() {
            // given
            FestivalV1lListRequest request = new FestivalV1lListRequest(SchoolRegion.대구.name(),
                FestivalFilter.PLANNED.name(), 10,
                null,
                null);

            // when
            FestivalV1ListResponse response = festivalV1QueryService.findFestivals(request);
            System.out.println(response.isLastPage());
            System.out.println(response);

            // then
            assertSoftly(softAssertions -> {
                assertThat(response.isLastPage()).isTrue();
                assertThat(response.festivals()).hasSize(3);
                assertThat(
                    response.festivals().get(1).startDate()
                        .isEqual(response.festivals().get(2).startDate())).isTrue();
                assertThat(response.festivals().get(1).id()).isLessThan(response.festivals().get(2).id());
            });
        }

        @Test
        void 진행_예정은_날짜가_시작_날짜가_빠른_순으로_반환된다() {
            // given
            FestivalV1lListRequest fistRequest = new FestivalV1lListRequest(SchoolRegion.대구.name(),
                FestivalFilter.PLANNED.name(), 1,
                null, null);
            FestivalV1ListResponse firstResponse = festivalV1QueryService.findFestivals(fistRequest);

            FestivalV1Response firstFestival = firstResponse.festivals().get(0);
            FestivalV1lListRequest secondRequest = new FestivalV1lListRequest(SchoolRegion.대구.name(),
                FestivalFilter.PLANNED.name(), 1,
                firstFestival.id(), firstFestival.startDate().toString());
            FestivalV1ListResponse secondResponse = festivalV1QueryService.findFestivals(secondRequest);

            FestivalV1Response secondFestival = secondResponse.festivals().get(0);
            FestivalV1lListRequest thirdRequest = new FestivalV1lListRequest(SchoolRegion.대구.name(),
                FestivalFilter.PLANNED.name(), 1,
                secondFestival.id(), secondFestival.startDate().toString());
            FestivalV1ListResponse thirdResponse = festivalV1QueryService.findFestivals(thirdRequest);

            FestivalV1Response thirdFestival = thirdResponse.festivals().get(0);

            // when && then
            assertSoftly(softAssertions -> {
                assertThat(firstFestival.startDate()).isBeforeOrEqualTo(secondFestival.startDate());
                assertThat(secondFestival.startDate()).isBeforeOrEqualTo(thirdFestival.startDate());
                assertThat(secondFestival.id() < thirdFestival.id()).isTrue();
            });
        }

        @Test
        void 진행_중인_축제는_시작_날짜가_느린_순으로_반환된다() {
            // given
            FestivalV1lListRequest fistRequest = new FestivalV1lListRequest(SchoolRegion.서울.name(),
                FestivalFilter.PROGRESS.name(), 1,
                null, null);
            FestivalV1ListResponse firstResponse = festivalV1QueryService.findFestivals(fistRequest);
            FestivalV1Response firstFestival = firstResponse.festivals().get(0);

            FestivalV1lListRequest secondRequest = new FestivalV1lListRequest(SchoolRegion.서울.name(),
                FestivalFilter.PROGRESS.name(), 1,
                firstFestival.id(), firstFestival.startDate().toString());
            FestivalV1ListResponse secondResponse = festivalV1QueryService.findFestivals(secondRequest);
            FestivalV1Response secondFestival = secondResponse.festivals().get(0);

            FestivalV1lListRequest thirdRequest = new FestivalV1lListRequest(SchoolRegion.서울.name(),
                FestivalFilter.PROGRESS.name(), 1,
                secondFestival.id(), secondFestival.startDate().toString());
            FestivalV1ListResponse thirdResponse = festivalV1QueryService.findFestivals(thirdRequest);
            FestivalV1Response thirdFestival = thirdResponse.festivals().get(0);

            FestivalV1lListRequest fourthRequest = new FestivalV1lListRequest(SchoolRegion.서울.name(),
                FestivalFilter.PROGRESS.name(), 1,
                thirdFestival.id(), thirdFestival.startDate().toString());
            FestivalV1ListResponse fourthResponse = festivalV1QueryService.findFestivals(fourthRequest);
            FestivalV1Response fourthFestival = fourthResponse.festivals().get(0);

            // when && then
            assertSoftly(softAssertions -> {
                assertThat(firstFestival.startDate()).isAfter(secondFestival.startDate());
                assertThat(secondFestival.startDate()).isAfter(thirdFestival.startDate());
                assertThat(thirdFestival.startDate()).isAfter(fourthFestival.startDate());
            });
        }

        @Test
        void 진행_중인_축제에_같은_날짜가_있으면_id가_작은_것_부터_보낸다() {
            // given
            FestivalV1lListRequest fistRequest = new FestivalV1lListRequest(SchoolRegion.서울.name(),
                FestivalFilter.PROGRESS.name(), 4,
                null, null);
            FestivalV1ListResponse firstResponse = festivalV1QueryService.findFestivals(fistRequest);
            FestivalV1Response firstLastFestival = firstResponse.festivals().get(3);

            FestivalV1lListRequest secondRequest = new FestivalV1lListRequest(SchoolRegion.서울.name(),
                FestivalFilter.PROGRESS.name(), 1,
                firstLastFestival.id(), firstLastFestival.startDate().toString());
            FestivalV1ListResponse secondResponse = festivalV1QueryService.findFestivals(secondRequest);
            FestivalV1Response secondLastFestival = secondResponse.festivals().get(0);

            // when && then
            assertSoftly(softAssertions -> {
                assertThat(firstLastFestival.startDate()).isEqualTo(secondLastFestival.startDate());
                assertThat(firstLastFestival.id() < secondLastFestival.id()).isTrue();
                assertThat(secondResponse.isLastPage()).isTrue();
            });
        }
    }
}
