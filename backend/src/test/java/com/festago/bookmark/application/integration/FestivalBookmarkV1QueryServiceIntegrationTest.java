package com.festago.bookmark.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.bookmark.application.FestivalBookmarkV1QueryService;
import com.festago.bookmark.application.command.FestivalBookmarkCommandService;
import com.festago.bookmark.dto.FestivalBookmarkV1Response;
import com.festago.bookmark.repository.FestivalBookmarkOrder;
import com.festago.festival.application.command.FestivalCreateService;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.member.repository.MemberRepository;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.MemberFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalBookmarkV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolCommandService schoolCommandService;

    @Autowired
    FestivalCreateService festivalCreateService;

    @Autowired
    FestivalBookmarkCommandService festivalBookmarkCommandService;

    @Autowired
    FestivalBookmarkV1QueryService festivalBookmarkV1QueryService;

    @Autowired
    MemberRepository memberRepository;

    Long 회원A_식별자;
    Long 회원B_식별자;
    Long 회원C_식별자;

    Long 테코대학교_식별자;
    Long 우테대학교_식별자;

    Long 테코대학교_봄_축제_식별자;
    Long 우테대학교_여름_축제_식별자;
    Long 우테대학교_가을_축제_식별자;

    @BeforeEach
    void setUp() {
        회원A_식별자 = memberRepository.save(MemberFixture.builder().socialId("1").nickname("회원A").build()).getId();
        회원B_식별자 = memberRepository.save(MemberFixture.builder().socialId("2").nickname("회원B").build()).getId();
        회원C_식별자 = memberRepository.save(MemberFixture.builder().socialId("3").nickname("회원C").build()).getId();

        테코대학교_식별자 = createSchool("테코대학교");
        우테대학교_식별자 = createSchool("우테대학교");

        테코대학교_봄_축제_식별자 = createFestival("테코대학교 봄 축제", 테코대학교_식별자, LocalDate.parse("2077-03-01"));
        우테대학교_여름_축제_식별자 = createFestival("우테대학교 여름 축제", 우테대학교_식별자, LocalDate.parse("2077-07-01"));
        우테대학교_가을_축제_식별자 = createFestival("우테대학교 가을 축제", 우테대학교_식별자, LocalDate.parse("2077-10-01"));
    }

    private Long createSchool(String schoolName) {
        return schoolCommandService.createSchool(new SchoolCreateCommand(
            schoolName,
            schoolName + ".ac.kr",
            SchoolRegion.서울,
            "https://image.com/logo.png",
            "https://image.com/background.png"
        ));
    }

    private Long createFestival(String festivalName, Long schoolId, LocalDate startDate) {
        return festivalCreateService.createFestival(new FestivalCreateCommand(
            festivalName,
            startDate,
            startDate.plusDays(2),
            "https://image.com/posterimage.png",
            schoolId
        ));
    }

    @Nested
    class findBookmarkedFestivalIds {

        @Test
        void 회원의_식별자로_북마크한_축제의_식별자를_조회한다() {
            // given
            festivalBookmarkCommandService.save(회원A_식별자, 테코대학교_봄_축제_식별자);
            festivalBookmarkCommandService.save(회원B_식별자, 우테대학교_여름_축제_식별자);
            festivalBookmarkCommandService.save(회원B_식별자, 우테대학교_가을_축제_식별자);

            // when
            var 회원A_축제_북마크_식별자_목록 = festivalBookmarkV1QueryService.findBookmarkedFestivalIds(회원A_식별자);
            var 회원B_축제_북마크_식별자_목록 = festivalBookmarkV1QueryService.findBookmarkedFestivalIds(회원B_식별자);
            var 회원C_축제_북마크_식별자_목록 = festivalBookmarkV1QueryService.findBookmarkedFestivalIds(회원C_식별자);

            // then
            assertSoftly(softly -> {
                softly.assertThat(회원A_축제_북마크_식별자_목록).containsExactlyInAnyOrder(테코대학교_봄_축제_식별자);
                softly.assertThat(회원B_축제_북마크_식별자_목록).containsExactlyInAnyOrder(우테대학교_여름_축제_식별자, 우테대학교_가을_축제_식별자);
                softly.assertThat(회원C_축제_북마크_식별자_목록).isEmpty();
            });
        }
    }

    @Nested
    class findBookmarkedFestivals {

        @Test
        void 북마크를_등록한_시간의_내림차순으로_조회할_수_있다() {
            // given
            festivalBookmarkCommandService.save(회원A_식별자, 우테대학교_여름_축제_식별자);
            festivalBookmarkCommandService.save(회원A_식별자, 테코대학교_봄_축제_식별자);
            festivalBookmarkCommandService.save(회원A_식별자, 우테대학교_가을_축제_식별자);

            // when
            var 회원A_북마크_축제_정보_목록 = festivalBookmarkV1QueryService.findBookmarkedFestivals(
                회원A_식별자,
                List.of(테코대학교_봄_축제_식별자, 우테대학교_여름_축제_식별자, 우테대학교_가을_축제_식별자),
                FestivalBookmarkOrder.BOOKMARK
            );

            // then
            assertThat(회원A_북마크_축제_정보_목록)
                .map(FestivalBookmarkV1Response::festival)
                .map(FestivalV1Response::id)
                .containsExactly(우테대학교_가을_축제_식별자, 테코대학교_봄_축제_식별자, 우테대학교_여름_축제_식별자);
        }

        @Test
        void 축제의_시작_시간의_오름차순으로_조회할_수_있다() {
            // given
            festivalBookmarkCommandService.save(회원A_식별자, 우테대학교_여름_축제_식별자);
            festivalBookmarkCommandService.save(회원A_식별자, 테코대학교_봄_축제_식별자);
            festivalBookmarkCommandService.save(회원A_식별자, 우테대학교_가을_축제_식별자);

            // when
            var 회원A_북마크_축제_정보_목록 = festivalBookmarkV1QueryService.findBookmarkedFestivals(
                회원A_식별자,
                List.of(우테대학교_가을_축제_식별자, 우테대학교_여름_축제_식별자, 테코대학교_봄_축제_식별자),
                FestivalBookmarkOrder.FESTIVAL
            );

            // then
            assertThat(회원A_북마크_축제_정보_목록)
                .map(FestivalBookmarkV1Response::festival)
                .map(FestivalV1Response::id)
                .containsExactly(테코대학교_봄_축제_식별자, 우테대학교_여름_축제_식별자, 우테대학교_가을_축제_식별자);
        }

        @Test
        void 북마크에_등록되지_않은_축제_식별자를_보내면_해당_축제는_조회되지_않는다() {
            // given
            festivalBookmarkCommandService.save(회원A_식별자, 테코대학교_봄_축제_식별자);

            // when
            var 회원A_북마크_축제_정보_목록 = festivalBookmarkV1QueryService.findBookmarkedFestivals(
                회원A_식별자,
                List.of(우테대학교_여름_축제_식별자, 우테대학교_가을_축제_식별자),
                FestivalBookmarkOrder.BOOKMARK
            );

            assertThat(회원A_북마크_축제_정보_목록).isEmpty();
        }
    }
}
