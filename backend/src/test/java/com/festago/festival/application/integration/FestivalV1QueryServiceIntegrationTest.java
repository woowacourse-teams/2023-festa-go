package com.festago.festival.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import com.festago.festival.application.FestivalV1QueryService;
import com.festago.festival.application.command.FestivalCreateService;
import com.festago.festival.dto.FestivalV1QueryRequest;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.festival.repository.FestivalFilter;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.TimeInstantProvider;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

// TODO Repository 사용하지 않고 Service로 데이터 세팅하도록 변경
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
class FestivalV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalV1QueryService festivalV1QueryService;

    @Autowired
    FestivalCreateService festivalCreateService;

    @Autowired
    SchoolCommandService schoolCommandService;

    @Autowired
    Clock clock;

    LocalDate now = LocalDate.parse("2077-07-10");

    // 진행중
    Long 서울대학교_8일_12일_축제_식별자;
    Long 서울대학교_6일_12일_축제_식별자;
    Long 대구대학교_9일_12일_축제_식별자;
    Long 부산대학교_6일_13일_축제_식별자;
    Long 부산대학교_6일_12일_축제_식별자;

    // 진행 예정
    Long 대구대학교_13일_14일_축제_식별자;
    Long 대구대학교_12일_14일_축제_식별자;
    Long 부산대학교_12일_14일_축제_식별자;

    /**
     * 현재 시간 <p> 2023년 7월 10일 <p>
     * <p>
     * 식별자는 순서대로 오름차순<p>
     * <p>
     * 진행 중 축제 5개 <p> 서울대학교 8~12일 <p> 서울대학교 6~12일 <p> 대구대학교 9~12일 <p> 부산대학교 6~13일 <p> 부산대학교 6~12일 <p>
     * <p>
     * 진행 예정 축제 3개 <p> 대구대학교 13~14일 <p> 대구대학교 12~14일 <p> 부산대학교 12~14일
     */
    @BeforeEach
    void setting() {
        Long 서울대학교_식별자 = createSchool("서울대학교", "seoul.ac.kr", SchoolRegion.서울);
        Long 부산대학교_식별자 = createSchool("부산대학교", "busan.ac.kr", SchoolRegion.부산);
        Long 대구대학교_식별자 = createSchool("대구대학교", "daegu.ac.kr", SchoolRegion.대구);

        // 진행 중
        서울대학교_8일_12일_축제_식별자 = createFestival("서울대학교_8일_12일_축제", now.minusDays(2), now.plusDays(2), 서울대학교_식별자);
        서울대학교_6일_12일_축제_식별자 = createFestival("서울대학교_6일_12일_축제", now.minusDays(4), now.plusDays(2), 서울대학교_식별자);
        대구대학교_9일_12일_축제_식별자 = createFestival("대구대학교_9일_12일_축제", now.minusDays(1), now.plusDays(2), 대구대학교_식별자);
        부산대학교_6일_13일_축제_식별자 = createFestival("부산대학교_6일_13일_축제", now.minusDays(4), now.plusDays(3), 부산대학교_식별자);
        부산대학교_6일_12일_축제_식별자 = createFestival("부산대학교_6일_12일_축제", now.minusDays(4), now.plusDays(2), 부산대학교_식별자);

        // 진행 예정
        대구대학교_13일_14일_축제_식별자 = createFestival("대구대학교_13일_14일_축제", now.plusDays(3), now.plusDays(4), 대구대학교_식별자);
        부산대학교_12일_14일_축제_식별자 = createFestival("부산대학교_12일_14일_축제", now.plusDays(2), now.plusDays(4), 부산대학교_식별자);
        대구대학교_12일_14일_축제_식별자 = createFestival("대구대학교_12일_14일_축제", now.plusDays(2), now.plusDays(4), 대구대학교_식별자);

        given(clock.instant())
            .willReturn(TimeInstantProvider.from(now));
    }

    private Long createSchool(String schoolName, String domain, SchoolRegion region) {
        return schoolCommandService.createSchool(new SchoolCreateCommand(
            schoolName,
            domain,
            region,
            "https://image.com/logo.png",
            "https://image.com/background.png"
        ));
    }

    private Long createFestival(String festivalName, LocalDate startDate, LocalDate endDate, Long schoolId) {
        return festivalCreateService.createFestival(new FestivalCreateCommand(
            festivalName, startDate, endDate, "https://image.com/posterImage.png", schoolId
        ));
    }

    @Nested
    class 지역_필터_미적용 {

        @Test
        void 진행_중_축제는_5개_이다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.ANY, FestivalFilter.PROGRESS, null, null);

            // when
            var actual = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.isLast()).isTrue();
                softly.assertThat(actual.getContent()).hasSize(5);
            });
        }

        @Test
        void 진행_예정_축제는_3개_이다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.ANY, FestivalFilter.PLANNED, null, null);

            // when
            var actual = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.isLast()).isTrue();
                softly.assertThat(actual.getContent()).hasSize(3);
            });
        }

        @Test
        void 원하는_갯수의_축제를_조회하면_마지막_페이지_여부를_알_수_있다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.ANY, FestivalFilter.PROGRESS, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(4), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.isLast()).isFalse();
                softly.assertThat(response.getContent()).hasSize(4);
            });
        }

        @Test
        void 진행_예정_축제는_시작_날짜가_빠른_순으로_정렬되고_시작_날짜가_같으면_식별자의_오름차순으로_정렬되어_반환된다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.ANY, FestivalFilter.PLANNED, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertThat(response.getContent())
                .map(FestivalV1Response::id)
                .containsExactly(
                    부산대학교_12일_14일_축제_식별자,
                    대구대학교_12일_14일_축제_식별자,
                    대구대학교_13일_14일_축제_식별자
                );
        }

        @Test
        void 진행_중_축제는_시작_날짜가_느린_순으로_정렬되고_시작_날짜가_같으면_식별자의_오름차순으로_정렬되어_반환된다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.ANY, FestivalFilter.PROGRESS, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertThat(response.getContent())
                .map(FestivalV1Response::id)
                .containsExactly(
                    대구대학교_9일_12일_축제_식별자,
                    서울대학교_8일_12일_축제_식별자,
                    서울대학교_6일_12일_축제_식별자,
                    부산대학교_6일_13일_축제_식별자,
                    부산대학교_6일_12일_축제_식별자
                );
        }

        @Test
        void 커서_기반_페이징이_적용되어야_한다() {
            // given
            var firstRequest = new FestivalV1QueryRequest(SchoolRegion.ANY, FestivalFilter.PROGRESS, null, null);
            var firstResponse = festivalV1QueryService.findFestivals(Pageable.ofSize(2), firstRequest);
            var lastElement = firstResponse.getContent().get(1);
            var secondRequest = new FestivalV1QueryRequest(SchoolRegion.ANY, FestivalFilter.PROGRESS, lastElement.id(),
                lastElement.startDate());

            // when
            var secondResponse = festivalV1QueryService.findFestivals(Pageable.ofSize(5), secondRequest);

            // then
            assertSoftly(softly -> {
                softly.assertThat(firstResponse.hasNext()).isTrue();
                softly.assertThat(firstResponse.getSize()).isEqualTo(2);
                softly.assertThat(secondResponse.hasNext()).isFalse();
                softly.assertThat(secondResponse.getContent())
                    .map(FestivalV1Response::id)
                    .containsExactly(
                        서울대학교_6일_12일_축제_식별자,
                        부산대학교_6일_13일_축제_식별자,
                        부산대학교_6일_12일_축제_식별자
                    );
            });
        }
    }

    @Nested
    class 지역_필터_적용 {

        @Test
        void 지역이_서울인_진행_중_축제는_2개_이다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.서울, FestivalFilter.PROGRESS, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.isLast()).isTrue();
                softly.assertThat(response.getContent()).hasSize(2);
            });
        }

        @Test
        void 지역이_대구인_진행_중_축제는_1개_이다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.대구, FestivalFilter.PROGRESS, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.isLast()).isTrue();
                softly.assertThat(response.getContent()).hasSize(1);
            });
        }

        @Test
        void 지역이_부산인_진행_중_축제는_2개_이다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.부산, FestivalFilter.PROGRESS, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.isLast()).isTrue();
                softly.assertThat(response.getContent()).hasSize(2);
            });
        }

        @Test
        void 지역이_서울인_진행_예정_축제는_0개_이다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.서울, FestivalFilter.PLANNED, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.isLast()).isTrue();
                softly.assertThat(response.getContent()).isEmpty();
            });
        }

        @Test
        void 지역이_부산인_진행_예정_축제는_1개_이다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.부산, FestivalFilter.PLANNED, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.isLast()).isTrue();
                softly.assertThat(response.getContent()).hasSize(1);
            });
        }

        @Test
        void 지역이_대구인_진행_예정_축제는_2개_이다() {
            // given
            var request = new FestivalV1QueryRequest(SchoolRegion.대구, FestivalFilter.PLANNED, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.isLast()).isTrue();
                softly.assertThat(response.getContent()).hasSize(2);
            });
        }

        @ParameterizedTest
        @MethodSource("지역별_진행_예정_축제_이름")
        void 진행_예정_축제는_시작_날짜가_빠른_순으로_정렬되고_시작_날짜가_같으면_식별자의_오름차순으로_정렬되어_반환된다(
            SchoolRegion region,
            List<String> festivalNames
        ) {
            // given
            var request = new FestivalV1QueryRequest(region, FestivalFilter.PLANNED, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertThat(response.getContent())
                .map(FestivalV1Response::name)
                .containsExactlyElementsOf(festivalNames);
        }

        private static Stream<Arguments> 지역별_진행_예정_축제_이름() {
            return Stream.of(
                Arguments.of(SchoolRegion.서울, List.of()),
                Arguments.of(SchoolRegion.대구, List.of(
                    "대구대학교_12일_14일_축제",
                    "대구대학교_13일_14일_축제"
                )),
                Arguments.of(SchoolRegion.부산, List.of(
                    "부산대학교_12일_14일_축제"
                ))
            );
        }

        @ParameterizedTest
        @MethodSource("지역별_진행_중_축제_이름")
        void 진행_중_축제는_시작_날짜가_느린_순으로_정렬되고_시작_날짜가_같으면_식별자의_오름차순으로_정렬되어_반환된다(
            SchoolRegion region,
            List<String> festivalNames
        ) {
            // given
            var request = new FestivalV1QueryRequest(region, FestivalFilter.PROGRESS, null, null);

            // when
            var response = festivalV1QueryService.findFestivals(Pageable.ofSize(10), request);

            // then
            assertThat(response.getContent())
                .map(FestivalV1Response::name)
                .containsExactlyElementsOf(festivalNames);
        }

        private static Stream<Arguments> 지역별_진행_중_축제_이름() {
            return Stream.of(
                Arguments.of(SchoolRegion.서울, List.of(
                    "서울대학교_8일_12일_축제",
                    "서울대학교_6일_12일_축제"
                )),
                Arguments.of(SchoolRegion.대구, List.of(
                    "대구대학교_9일_12일_축제"
                )),
                Arguments.of(SchoolRegion.부산, List.of(
                    "부산대학교_6일_13일_축제",
                    "부산대학교_6일_12일_축제"
                ))
            );
        }
    }
}
