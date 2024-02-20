package com.festago.school.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.querydsl.SearchCondition;
import com.festago.school.application.v1.AdminSchoolV1QueryService;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.v1.AdminSchoolV1Response;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminSchoolV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    AdminSchoolV1QueryService adminSchoolV1QueryService;

    @Autowired
    SchoolRepository schoolRepository;

    School 테코대학교;
    School 우테대학교;
    School 글렌대학교;

    @BeforeEach
    void setUp() {
        테코대학교 = schoolRepository.save(new School("teco.ac.kr", "테코대학교", SchoolRegion.서울));
        우테대학교 = schoolRepository.save(new School("wote.ac.kr", "우테대학교", SchoolRegion.서울));
        글렌대학교 = schoolRepository.save(new School("glen.ac.kr", "글렌대학교", SchoolRegion.대구));
    }

    @Nested
    class findAll {

        @Test
        void 정렬이_되어야_한다() {
            // given
            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
            SearchCondition searchCondition = new SearchCondition("", "", pageable);

            // when
            Page<AdminSchoolV1Response> response = adminSchoolV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .map(AdminSchoolV1Response::name)
                .containsExactly(글렌대학교.getName(), 우테대학교.getName(), 테코대학교.getName());
        }

        @Test
        void 식별자로_검색이_되어야_한다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("id", 글렌대학교.getId().toString(), pageable);

            // when
            Page<AdminSchoolV1Response> response = adminSchoolV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .map(AdminSchoolV1Response::name)
                .containsExactlyInAnyOrder(글렌대학교.getName());
        }

        @Test
        void 지역으로_검색이_되어야_한다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("region", "서울", pageable);

            // when
            Page<AdminSchoolV1Response> response = adminSchoolV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .map(AdminSchoolV1Response::name)
                .containsExactlyInAnyOrder(우테대학교.getName(), 테코대학교.getName());
        }

        @Test
        void 도메인이_포함된_검색이_되어야_한다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("domain", "wote", pageable);

            // when
            Page<AdminSchoolV1Response> response = adminSchoolV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .map(AdminSchoolV1Response::name)
                .containsExactly(우테대학교.getName());
        }

        @Test
        void 이름이_포함된_검색이_되어야_한다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("name", "글렌", pageable);

            // when
            Page<AdminSchoolV1Response> response = adminSchoolV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .map(AdminSchoolV1Response::name)
                .containsExactly(글렌대학교.getName());
        }

        @Test
        void 검색_필터가_비어있으면_필터링이_적용되지_않는다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("", "글렌", pageable);

            // when
            Page<AdminSchoolV1Response> response = adminSchoolV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .hasSize(3);
        }

        @Test
        void 검색어가_비어있으면_필터링이_적용되지_않는다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("id", "", pageable);

            // when
            Page<AdminSchoolV1Response> response = adminSchoolV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .hasSize(3);
        }

        @Test
        void 페이지네이션이_적용_되어야_한다() {
            // given
            Pageable pageable = PageRequest.of(0, 2);
            SearchCondition searchCondition = new SearchCondition("", "", pageable);

            // when
            Page<AdminSchoolV1Response> response = adminSchoolV1QueryService.findAll(searchCondition);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.getSize()).isEqualTo(2);
                softly.assertThat(response.getTotalPages()).isEqualTo(2);
                softly.assertThat(response.getTotalElements()).isEqualTo(3);
            });
        }
    }

    @Nested
    class findById {

        @Test
        void 식별자로_조회가_되어야_한다() {
            // given
            Long 테코대학교_식별자 = 테코대학교.getId();

            // when
            AdminSchoolV1Response response = adminSchoolV1QueryService.findById(테코대학교_식별자);

            // then
            assertThat(response.name()).isEqualTo("테코대학교");
        }

        @Test
        void 식별자로_찾을_수_없으면_예외가_발생한다() {
            // given
            Long invalidId = 4885L;

            // when
            assertThatThrownBy(() -> adminSchoolV1QueryService.findById(invalidId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.SCHOOL_NOT_FOUND.getMessage());
        }
    }
}
