package com.festago.admin.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.admin.application.AdminSocialMediaV1QueryService;
import com.festago.admin.dto.socialmedia.AdminSocialMediaV1Response;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.SocialMediaFixture;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminSocialMediaV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    AdminSocialMediaV1QueryService adminSocialMediaV1QueryService;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    @Nested
    class findById {

        @Test
        void 소셜미디어_식별자로_조회할_수_있다() {
            // given
            Long 테코대학교_식별자 = 1L;
            Long 소셜미디어_식별자 = socialMediaRepository.save(SocialMediaFixture.builder()
                .ownerId(테코대학교_식별자)
                .ownerType(OwnerType.SCHOOL)
                .name("테코대학교 소셜미디어")
                .build()).getId();

            // when
            var actual = adminSocialMediaV1QueryService.findById(소셜미디어_식별자);

            // then
            assertThat(actual.name()).isEqualTo("테코대학교 소셜미디어");
        }

        @Test
        void 식별자에_대한_소셜미디어가_존재하지_않으면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> adminSocialMediaV1QueryService.findById(4885L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.SOCIAL_MEDIA_NOT_FOUND.getMessage());
        }
    }

    @Nested
    class findByOwnerIdAndOwnerType {

        @Test
        void ownerId와_ownerType으로_해당하는_소셜미디어를_모두_조회할_수_있다() {
            // given
            Long 테코대학교_식별자 = 1L;
            var expect = Stream.of(SocialMediaType.INSTAGRAM, SocialMediaType.X, SocialMediaType.YOUTUBE)
                .map(mediaType -> socialMediaRepository.save(SocialMediaFixture.builder()
                    .ownerId(테코대학교_식별자)
                    .ownerType(OwnerType.SCHOOL)
                    .mediaType(mediaType)
                    .build())
                )
                .map(SocialMedia::getId)
                .toList();

            // when
            var actual = adminSocialMediaV1QueryService.findByOwnerIdAndOwnerType(테코대학교_식별자, OwnerType.SCHOOL);

            // then
            assertThat(actual)
                .map(AdminSocialMediaV1Response::id)
                .containsExactlyInAnyOrderElementsOf(expect);
        }
    }
}
