package com.festago.school.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.application.v1.SchoolV1QueryService;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalV1Response;
import com.festago.school.repository.SchoolRepository;
import com.festago.school.repository.v1.SchoolFestivalV1SearchCondition;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolV1QueryService schoolV1QueryService;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Nested
    class 학교_상세_정보_조회 {

        @Test
        void 해당하는_학교가_존재하지_않으면_예외() {
            // when && then
            assertThatThrownBy(() -> schoolV1QueryService.findDetailById(-1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 학교입니다.");
        }

        @Test
        void 학교에_소셜미디어가_존재하지_않아도_조회된다() {
            // given
            School school = schoolRepository.save(SchoolFixture.builder().build());

            // when
            SchoolDetailV1Response actual = schoolV1QueryService.findDetailById(school.getId());

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual).isNotNull();
                softly.assertThat(actual.socialMedias()).isEmpty();
            });
        }

        @Test
        void 아티스트의_소셜미디어는_아이디가_같아도_조회되지_않는다() {
            // given
            School school = schoolRepository.save(SchoolFixture.builder().build());
            saveSocialMedia(school.getId(), OwnerType.SCHOOL, SocialMediaType.X);
            saveSocialMedia(school.getId(), OwnerType.ARTIST, SocialMediaType.YOUTUBE);

            // when
            SchoolDetailV1Response actual = schoolV1QueryService.findDetailById(school.getId());

            // then
            assertThat(actual.socialMedias()).hasSize(1);
        }

        @Test
        void 학교와_포함된_소셜미디어를_모두_조회한다() {
            // given
            School school = schoolRepository.save(SchoolFixture.builder().build());
            saveSocialMedia(school.getId(), OwnerType.SCHOOL, SocialMediaType.X);
            saveSocialMedia(school.getId(), OwnerType.SCHOOL, SocialMediaType.YOUTUBE);

            // when
            SchoolDetailV1Response actual = schoolV1QueryService.findDetailById(school.getId());

            // then
            assertThat(actual.socialMedias()).hasSize(2);
        }

        private void saveSocialMedia(Long ownerId, OwnerType ownerType, SocialMediaType mediaType) {
            socialMediaRepository.save(
                new SocialMedia(ownerId, ownerType, mediaType,
                    "defaultName", "www.profileImageUrl.com", "www.url.com")
            );
        }
    }

    @Nested
    class 학교별_축제_페이징_조회 {

        School school;
        LocalDate today = LocalDate.now();

        @BeforeEach
        void setUp() {
            school = schoolRepository.save(SchoolFixture.builder().build());
        }

        @Test
        void 과거_축제만_가져온다() {
            // given

            // 진행중
            saveFestival(today, today.plusDays(1));
            saveFestival(today, today.plusDays(1));

            // 진행예정
            saveFestival(today.plusDays(1), today.plusDays(2));
            saveFestival(today.plusDays(1), today.plusDays(2));

            // 종료
            Festival lastFestival = saveFestival(today.minusDays(3), today.minusDays(1));

            var searchCondition = new SchoolFestivalV1SearchCondition(null, null, true, Pageable.ofSize(10));
            // when
            List<SchoolFestivalV1Response> actual = schoolV1QueryService.findFestivalsBySchoolId(
                school.getId(), today, searchCondition).getContent();

            // then
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).id()).isEqualTo(lastFestival.getId());
        }

        @Test
        void 현재_혹은_예정_축제만_가져온다() {
            // given

            // 진행 혹은 예정 축제
            saveFestival(today, today.plusDays(1));
            saveFestival(today.plusDays(1), today.plusDays(2));

            var searchCondition = new SchoolFestivalV1SearchCondition(null, null, false, Pageable.ofSize(10));

            // 종료 축제
            saveFestival(today.minusDays(3), today.minusDays(1));

            // when
            List<SchoolFestivalV1Response> actual = schoolV1QueryService.findFestivalsBySchoolId(
                school.getId(), today, searchCondition).getContent();

            // then
            assertThat(actual).hasSize(2);
        }

        @Test
        void 현재_축제를_시작일자가_빠른순으로_조회한다() {
            // given
            saveFestival(today.plusDays(2), today.plusDays(3));
            saveFestival(today.plusDays(2), today.plusDays(3));
            Festival recentFestival = saveFestival(today, today.plusDays(1));
            var searchCondition = new SchoolFestivalV1SearchCondition(null, null, false, Pageable.ofSize(10));

            // when
            List<SchoolFestivalV1Response> actual = schoolV1QueryService.findFestivalsBySchoolId(
                school.getId(), today, searchCondition).getContent();

            // then
            assertThat(actual.get(0).id()).isEqualTo(recentFestival.getId());
        }

        @Test
        void 과거_축제를_종료일자가_느린순으로_조회한다() {
            // given
            saveFestival(today.minusDays(4), today.minusDays(3));
            saveFestival(today.minusDays(3), today.minusDays(2));
            Festival recentFestival = saveFestival(today.minusDays(3), today.minusDays(1));
            var searchCondition = new SchoolFestivalV1SearchCondition(null, null, true, Pageable.ofSize(10));

            // when
            List<SchoolFestivalV1Response> actual = schoolV1QueryService.findFestivalsBySchoolId(
                school.getId(), today, searchCondition).getContent();

            // then
            assertThat(actual.get(0).id()).isEqualTo(recentFestival.getId());
        }

        @Test
        void 페이징하여_현재_축제를_조회한다() {
            // given
            saveFestival(today, today.plusDays(3));
            Festival nextPageFirstReadFestival = saveFestival(today.plusDays(1), today.plusDays(1));
            Festival lastReadFestival = saveFestival(today, today.plusDays(1));
            saveFestival(today.plusDays(1), today.plusDays(1));
            saveFestival(today.plusDays(2), today.plusDays(2));
            var searchCondition = new SchoolFestivalV1SearchCondition(
                lastReadFestival.getId(), lastReadFestival.getStartDate(), false, Pageable.ofSize(2));

            // when
            List<SchoolFestivalV1Response> actual = schoolV1QueryService.findFestivalsBySchoolId(
                school.getId(), today, searchCondition).getContent();

            // then
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0).id()).isEqualTo(nextPageFirstReadFestival.getId());
        }

        @Test
        void 페이징하여_과거_축제를_조회한다() {
            // given
            LocalDate yesterday = today.minusDays(1);

            saveFestival(yesterday.minusDays(2), yesterday);
            Festival nextPageFirstReadFestival = saveFestival(yesterday.minusDays(3), yesterday);
            Festival lastReadFestival = saveFestival(yesterday.minusDays(2), yesterday);
            saveFestival(yesterday.minusDays(4), yesterday);
            saveFestival(yesterday.minusDays(4), yesterday);
            var searchCondition = new SchoolFestivalV1SearchCondition(
                lastReadFestival.getId(), lastReadFestival.getStartDate(), true, Pageable.ofSize(2));

            // when
            List<SchoolFestivalV1Response> actual = schoolV1QueryService.findFestivalsBySchoolId(
                school.getId(), today, searchCondition).getContent();

            // then
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0).id()).isEqualTo(nextPageFirstReadFestival.getId());
        }

        @Test
        void 다음_페이지가_존재한다() {
            // given
            saveFestival(today, today.plusDays(1));
            saveFestival(today, today.plusDays(1));
            var searchCondition = new SchoolFestivalV1SearchCondition(null, null, false, Pageable.ofSize(1));

            // when
            Slice<SchoolFestivalV1Response> actual = schoolV1QueryService.findFestivalsBySchoolId(
                school.getId(), today,
                searchCondition);

            // then
            assertThat(actual.hasNext()).isFalse();
        }

        @Test
        void 다음_페이지가_존재하지않는다() {
            // given
            saveFestival(today, today.plusDays(1));
            var searchCondition = new SchoolFestivalV1SearchCondition(null, null, false, Pageable.ofSize(1));

            // when
            Slice<SchoolFestivalV1Response> actual = schoolV1QueryService.findFestivalsBySchoolId(
                school.getId(), today, searchCondition);

            // then
            assertThat(actual.hasNext()).isTrue();
        }

        private Festival saveFestival(LocalDate startDate, LocalDate endDate) {
            return festivalRepository.save(
                FestivalFixture.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .school(school)
                    .build());
        }
    }
}
