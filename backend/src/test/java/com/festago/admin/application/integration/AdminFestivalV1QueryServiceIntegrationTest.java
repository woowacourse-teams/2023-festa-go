package com.festago.admin.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.admin.application.AdminFestivalV1QueryService;
import com.festago.admin.dto.AdminFestivalV1Response;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.querydsl.SearchCondition;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminFestivalV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    AdminFestivalV1QueryService adminFestivalV1QueryService;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    StageRepository stageRepository;

    LocalDate now = LocalDate.parse("2077-06-30");
    LocalDate tomorrow = now.plusDays(1);

    School 테코대학교;
    School 우테대학교;
    Festival 테코대학교_축제;
    Festival 테코대학교_공연_없는_축제;
    Festival 우테대학교_축제;
    Stage 테코대학교_공연;
    Stage 우테대학교_첫째날_공연;
    Stage 우테대학교_둘째날_공연;

    @BeforeEach
    void setUp() {
        LocalDateTime ticketOpenTime = now.atStartOfDay().minusWeeks(1);
        테코대학교 = schoolRepository.save(new School("teco.ac.kr", "테코대학교", SchoolRegion.서울));
        우테대학교 = schoolRepository.save(new School("wote.ac.kr", "우테대학교", SchoolRegion.서울));
        테코대학교_축제 = festivalRepository.save(new Festival("테코대학교 축제", now, now, 테코대학교));
        테코대학교_공연_없는_축제 = festivalRepository.save(new Festival("테코대학교 공연 없는 축제", tomorrow, tomorrow, 테코대학교));
        우테대학교_축제 = festivalRepository.save(new Festival("우테대학교 축제", now, tomorrow, 우테대학교));
        테코대학교_공연 = stageRepository.save(new Stage(now.atTime(18, 0), ticketOpenTime, 테코대학교_축제));
        우테대학교_첫째날_공연 = stageRepository.save(new Stage(now.atTime(18, 0), ticketOpenTime, 우테대학교_축제));
        우테대학교_둘째날_공연 = stageRepository.save(new Stage(tomorrow.atTime(18, 0), ticketOpenTime, 우테대학교_축제));
    }

    @Nested
    class findAll {

        @Test
        void 페이지네이션이_적용되어야_한다() {
            // given
            Pageable pageable = PageRequest.ofSize(2);
            SearchCondition searchCondition = new SearchCondition("", "", pageable);

            // when
            var response = adminFestivalV1QueryService.findAll(searchCondition);

            assertSoftly(softly -> {
                softly.assertThat(response.getSize()).isEqualTo(2);
                softly.assertThat(response.getTotalPages()).isEqualTo(2);
                softly.assertThat(response.getTotalElements()).isEqualTo(3);
            });
        }

        @Test
        void 공연의_수가_정확하게_반환되어야_한다() {
            // given
            Pageable pageable = PageRequest.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("", "", pageable);

            // when
            var response = adminFestivalV1QueryService.findAll(searchCondition);

            // then
            assertThat(response.getContent())
                .map(AdminFestivalV1Response::stageCount)
                .containsExactly(1L, 0L, 2L);
        }

        @Nested
        class 정렬 {

            @Test
            void 축제의_식별자로_정렬_되어야_한다() {
                // given
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
                SearchCondition searchCondition = new SearchCondition("", "", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                // then
                assertThat(response.getContent())
                    .map(AdminFestivalV1Response::id)
                    .containsExactly(우테대학교_축제.getId(), 테코대학교_공연_없는_축제.getId(), 테코대학교_축제.getId());
            }

            @Test
            void 축제의_이름으로_정렬이_되어야_한다() {
                // given
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
                SearchCondition searchCondition = new SearchCondition("", "", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                // then
                assertThat(response.getContent())
                    .map(AdminFestivalV1Response::name)
                    .containsExactly(우테대학교_축제.getName(), 테코대학교_공연_없는_축제.getName(), 테코대학교_축제.getName());
            }

            @Test
            void 학교의_이름으로_정렬이_되어야_한다() {
                // given
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "schoolName"));
                SearchCondition searchCondition = new SearchCondition("", "", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                // then
                assertThat(response.getContent())
                    .map(AdminFestivalV1Response::id)
                    .containsExactly(우테대학교_축제.getId(), 테코대학교_축제.getId(), 테코대학교_공연_없는_축제.getId());
            }

            @Test
            void 축제의_시작일으로_정렬이_되어야_한다() {
                // given
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "startDate"));
                SearchCondition searchCondition = new SearchCondition("", "", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                // then
                assertThat(response.getContent())
                    .map(AdminFestivalV1Response::id)
                    .containsExactly(테코대학교_축제.getId(), 우테대학교_축제.getId(), 테코대학교_공연_없는_축제.getId());
            }

            @Test
            void 축제의_종료일으로_정렬이_되어야_한다() {
                // given
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "endDate"));
                SearchCondition searchCondition = new SearchCondition("", "", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                // then
                System.out.println(response.getContent());
                assertThat(response.getContent())
                    .map(AdminFestivalV1Response::id)
                    .containsExactly(테코대학교_공연_없는_축제.getId(), 우테대학교_축제.getId(), 테코대학교_축제.getId());
            }

            @Test
            void 정렬_조건에_없으면_식별자의_오름차순으로_정렬이_되어야_한다() {
                // given
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "foo"));
                SearchCondition searchCondition = new SearchCondition("", "", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                // then
                assertThat(response.getContent())
                    .map(AdminFestivalV1Response::id)
                    .containsExactly(테코대학교_축제.getId(), 테코대학교_공연_없는_축제.getId(), 우테대학교_축제.getId());
            }
        }

        @Nested
        class 검색 {

            @Test
            void 축제의_식별자로_검색이_되어야_한다() {
                // given
                Pageable pageable = Pageable.ofSize(10);
                SearchCondition searchCondition = new SearchCondition("id", 테코대학교_축제.getId().toString(), pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                assertThat(response.getContent())
                    .map(AdminFestivalV1Response::id)
                    .containsExactly(테코대학교_축제.getId());
            }

            @Test
            void 축제의_이름이_포함된_검색이_되어야_한다() {
                // given
                Pageable pageable = Pageable.ofSize(10);
                SearchCondition searchCondition = new SearchCondition("name", "테코대학교", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                assertThat(response.getContent())
                    .map(AdminFestivalV1Response::id)
                    .containsExactly(테코대학교_축제.getId(), 테코대학교_공연_없는_축제.getId());
            }

            @Test
            void 학교의_이름이_포함된_검색이_되어야_한다() {
                // given
                Pageable pageable = Pageable.ofSize(10);
                SearchCondition searchCondition = new SearchCondition("schoolName", "우테대학교", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                assertThat(response.getContent())
                    .map(AdminFestivalV1Response::id)
                    .containsExactly(우테대학교_축제.getId());
            }

            @Test
            void 검색_필터가_비어있으면_필터링이_적용되지_않는다() {
                // given
                Pageable pageable = Pageable.ofSize(10);
                SearchCondition searchCondition = new SearchCondition("", "글렌", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                assertThat(response.getContent())
                    .hasSize(3);
            }

            @Test
            void 검색어가_비어있으면_필터링이_적용되지_않는다() {
                // given
                Pageable pageable = Pageable.ofSize(10);
                SearchCondition searchCondition = new SearchCondition("id", "", pageable);

                // when
                var response = adminFestivalV1QueryService.findAll(searchCondition);

                assertThat(response.getContent())
                    .hasSize(3);
            }
        }
    }

    @Nested
    class findDetail {

        @Test
        void 축제의_식별자로_조회할_수_있어야_한다() {
            // when
            var actual = adminFestivalV1QueryService.findDetail(우테대학교_축제.getId());

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.id()).isEqualTo(우테대학교_축제.getId());
                softly.assertThat(actual.name()).isEqualTo(우테대학교_축제.getName());
                softly.assertThat(actual.schoolId()).isEqualTo(우테대학교.getId());
                softly.assertThat(actual.schoolName()).isEqualTo(우테대학교.getName());
            });
        }

        @Test
        void 축제의_식별자에_해당하는_축제가_없으면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> adminFestivalV1QueryService.findDetail(4885L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.FESTIVAL_NOT_FOUND.getMessage());
        }
    }
}
