package com.festago.school.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalResponse;
import com.festago.school.repository.SchoolRepository;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.FestivalFixture;
import com.festago.support.SchoolFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolV1QueryServiceTest extends ApplicationIntegrationTest {

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
            assertThatThrownBy(() -> schoolV1QueryService.findById(-1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 학교입니다.");
        }

        @Test
        void 학교에_소셜미디어가_존재하지_않아도_조회된다() {
            // given
            School school = schoolRepository.save(SchoolFixture.school().build());

            // when
            SchoolDetailV1Response actual = schoolV1QueryService.findById(school.getId());

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual).isNotNull();
                softly.assertThat(actual.socialMedias()).isEmpty();
            });
        }

        @Test
        void 아티스트의_소셜미디어는_아이디가_같아도_조회되지_않는다() {
            // given
            School school = schoolRepository.save(SchoolFixture.school().build());
            saveSocialMedia(school.getId(), OwnerType.SCHOOL, SocialMediaType.X);
            saveSocialMedia(school.getId(), OwnerType.ARTIST, SocialMediaType.YOUTUBE);

            // when
            SchoolDetailV1Response actual = schoolV1QueryService.findById(school.getId());

            // then
            assertThat(actual.socialMedias()).hasSize(1);
        }


        @Test
        void 학교와_포함된_소셜미디어를_모두_조회한다() {
            // given
            School school = schoolRepository.save(SchoolFixture.school().build());
            saveSocialMedia(school.getId(), OwnerType.SCHOOL, SocialMediaType.X);
            saveSocialMedia(school.getId(), OwnerType.SCHOOL, SocialMediaType.YOUTUBE);

            // when
            SchoolDetailV1Response actual = schoolV1QueryService.findById(school.getId());

            // then
            assertThat(actual.socialMedias()).hasSize(2);
        }

        private void saveSocialMedia(Long ownerId, OwnerType ownerType, SocialMediaType mediaType) {
            socialMediaRepository.save(
                new SocialMedia(ownerId, ownerType, mediaType,
                    "defaultName", "www.logoUrl.com", "www.url.com")
            );
        }
    }

    @Test
    void 과거_축제만_가져온다() {
        // given
        LocalDate today = LocalDate.now();
        School school = schoolRepository.save(SchoolFixture.school().build());

        //진행중
        saveFestival(today, today.plusDays(1), school);
        saveFestival(today, today.plusDays(1), school);

        //진행예정
        saveFestival(today.plusDays(1), today.plusDays(2), school);
        saveFestival(today.plusDays(1), today.plusDays(2), school);

        //종료
        Festival lastFestival = saveFestival(today.minusDays(3), today.minusDays(1), school);

        // when
        List<SchoolFestivalResponse> actual = schoolV1QueryService.findAll(
            school.getId(), today, null, null, true, 10);

        // then
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).id()).isEqualTo(lastFestival.getId());
    }

    @Test
    void 현재_혹은_예정_축제만_가져온다() {
        // given
        LocalDate today = LocalDate.now();
        School school = schoolRepository.save(SchoolFixture.school().build());

        saveFestival(today, today.plusDays(1), school);
        saveFestival(today.plusDays(1), today.plusDays(2), school);
        saveFestival(today.minusDays(3), today.minusDays(1), school);
        // when
        List<SchoolFestivalResponse> actual = schoolV1QueryService.findAll(
            school.getId(), today, null, null, false, 10);

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 현재_축제를_시작일자가_빠른순으로_조회한다() {
        // given
        LocalDate today = LocalDate.now();
        School school = schoolRepository.save(SchoolFixture.school().build());

        saveFestival(today.plusDays(2), today.plusDays(3), school);
        saveFestival(today.plusDays(2), today.plusDays(3), school);
        Festival recentFestival = saveFestival(today, today.plusDays(1), school);

        // when
        List<SchoolFestivalResponse> actual = schoolV1QueryService.findAll(school.getId(), today, null, null, false,
            10);

        // then
        assertThat(actual.get(0).id()).isEqualTo(recentFestival.getId());
    }

    @Test
    void 과거_축제를_종료일자가_느린순으로_조회한다() {
        // given
        LocalDate today = LocalDate.now();
        School school = schoolRepository.save(SchoolFixture.school().build());

        saveFestival(today.minusDays(4), today.minusDays(3), school);
        saveFestival(today.minusDays(3), today.minusDays(2), school);
        Festival recentFestival = saveFestival(today.minusDays(3), today.minusDays(1), school);

        // when
        List<SchoolFestivalResponse> actual = schoolV1QueryService.findAll(school.getId(), today, null, null, true, 10);

        // then
        assertThat(actual.get(0).id()).isEqualTo(recentFestival.getId());
    }

    @Test
    void 페이징하여_현재_축제를_조회한다() {
        // given
        LocalDate today = LocalDate.now();
        School school = schoolRepository.save(SchoolFixture.school().build());

        saveFestival(today, today.plusDays(3), school);
        Festival nextPageFirstReadFestival = saveFestival(today.plusDays(1), today.plusDays(1), school);
        Festival lastReadFestival = saveFestival(today, today.plusDays(1), school);
        saveFestival(today.plusDays(1), today.plusDays(1), school);
        saveFestival(today.plusDays(2), today.plusDays(2), school);

        // when
        List<SchoolFestivalResponse> actual = schoolV1QueryService.findAll(school.getId(), today,
            lastReadFestival.getId(), lastReadFestival.getStartDate(), false, 2);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).id()).isEqualTo(nextPageFirstReadFestival.getId());
    }

    @Test
    void 페이징하여_과거_축제를_조회한다() {
        // given
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(2);
        School school = schoolRepository.save(SchoolFixture.school().build());

        saveFestival(yesterday.minusDays(2), yesterday, school);
        Festival nextPageFirstReadFestival = saveFestival(yesterday.minusDays(3), yesterday, school);
        Festival lastReadFestival = saveFestival(yesterday.minusDays(2), yesterday, school);
        saveFestival(yesterday.minusDays(4), yesterday, school);

        // when
        List<SchoolFestivalResponse> actual = schoolV1QueryService.findAll(school.getId(), today,
            lastReadFestival.getId(), lastReadFestival.getStartDate(), true, 2);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).id()).isEqualTo(nextPageFirstReadFestival.getId());
    }

    private Festival saveFestival(LocalDate startDate, LocalDate endDate, School school) {
        return festivalRepository.save(
            FestivalFixture.festival()
                .startDate(startDate)
                .endDate(endDate)
                .school(school)
                .build());
    }
}
