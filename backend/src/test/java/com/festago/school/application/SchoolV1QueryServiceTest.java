package com.festago.school.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.common.exception.NotFoundException;
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
    void 현재_진행중혹은_진행예정_축제만_가져온다() {
        // given
        LocalDate today = LocalDate.now();
        School school = schoolRepository.save(SchoolFixture.school().build());

        //진행중
        festivalRepository.save(
            FestivalFixture.festival()
                .startDate(today)
                .endDate(today.plusDays(1))
                .school(school)
                .build());
        festivalRepository.save(
            FestivalFixture.festival()
                .startDate(today)
                .endDate(today.plusDays(1))
                .school(school)
                .build());

        //진행예정
        festivalRepository.save(
            FestivalFixture.festival()
                .startDate(today.plusDays(1))
                .endDate(today.plusDays(2))
                .school(school)
                .build());

        festivalRepository.save(
            FestivalFixture.festival()
                .startDate(today.plusDays(1))
                .endDate(today.plusDays(2))
                .school(school)
                .build());


        //종료
        festivalRepository.save(
            FestivalFixture.festival()
                .startDate(today.minusDays(3))
                .endDate(today.minusDays(1))
                .school(school)
                .build());

        // when
        List<SchoolFestivalResponse> actual = schoolV1QueryService.findAll(
            school.getId(), today, 1L, today);

        // then
        assertThat(actual).hasSize(4);
        assertThat(actual.get(0).startDate()).isEqualTo(today);
    }
}
