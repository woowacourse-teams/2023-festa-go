package com.festago.socialmedia.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.repository.ArtistRepository;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.domain.School;
import com.festago.school.repository.MemorySchoolRepository;
import com.festago.school.repository.SchoolRepository;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.dto.command.SocialMediaCreateCommand;
import com.festago.socialmedia.dto.command.SocialMediaUpdateCommand;
import com.festago.socialmedia.repository.MemorySocialMediaRepository;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.SocialMediaFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SocialMediaCommandServiceTest {

    SocialMediaCommandService socialMediaCommandService;

    SocialMediaRepository socialMediaRepository;

    SchoolRepository schoolRepository;

    ArtistRepository artistRepository;

    @BeforeEach
    void setUp() {
        socialMediaRepository = new MemorySocialMediaRepository();
        schoolRepository = new MemorySchoolRepository();
        artistRepository = new MemoryArtistRepository();
        socialMediaCommandService = new SocialMediaCommandService(
            socialMediaRepository,
            schoolRepository,
            artistRepository
        );
    }

    @Nested
    class createSocialMedia {

        @Test
        void 중복된_소셜미디어가_있으면_예외() {
            // given
            School 테코대학교 = schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());

            SocialMedia socialMedia = socialMediaRepository.save(SocialMediaFixture.builder()
                .ownerId(테코대학교.getId())
                .ownerType(OwnerType.SCHOOL)
                .mediaType(SocialMediaType.INSTAGRAM)
                .build());

            // when & then
            var command = new SocialMediaCreateCommand(
                socialMedia.getOwnerId(),
                socialMedia.getOwnerType(),
                socialMedia.getMediaType(),
                socialMedia.getName(),
                socialMedia.getLogoUrl(),
                socialMedia.getUrl()
            );
            assertThatThrownBy(() -> socialMediaCommandService.createSocialMedia(command))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.DUPLICATE_SOCIAL_MEDIA.getMessage());
        }

        // TODO 다른 Owner에 대한 테스트를 어떻게 작성할지? ex) MethodSource
        @Test
        void 추가하려는_소셜미디어의_owner가_존재하지_않으면_예외() {
            // when & then
            var command = new SocialMediaCreateCommand(
                4885L,
                OwnerType.SCHOOL,
                SocialMediaType.INSTAGRAM,
                "테코대학교 인스타그램",
                "https://image.com/logo.png",
                "https://instagram.com/tecodaehak"
            );
            assertThatThrownBy(() -> socialMediaCommandService.createSocialMedia(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.SCHOOL_NOT_FOUND.getMessage());
        }

        @Test
        void 성공하면_소셜미디어가_저장된다() {
            // given
            School 테코대학교 = schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());

            // when
            var command = new SocialMediaCreateCommand(
                테코대학교.getId(),
                OwnerType.SCHOOL,
                SocialMediaType.INSTAGRAM,
                "테코대학교 인스타그램",
                "https://image.com/logo.png",
                "https://instagram.com/tecodaehak"
            );
            socialMediaCommandService.createSocialMedia(command);

            // then
            assertThat(socialMediaRepository.existsByOwnerIdAndOwnerTypeAndMediaType(
                테코대학교.getId(),
                OwnerType.SCHOOL,
                SocialMediaType.INSTAGRAM
            )).isTrue();
        }
    }

    @Nested
    class updateSocialMedia {

        @Test
        void 소셜미디어의_식별자에_대한_소셜미디어가_존재하지_않으면_예외() {
            // when & then
            var command = new SocialMediaUpdateCommand(
                "테코대학교 인스타그램",
                "https://instagram.com/tecodaehak",
                "https://image.com/logo.png"
            );
            assertThatThrownBy(() -> socialMediaCommandService.updateSocialMedia(4885L, command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.SOCIAL_MEDIA_NOT_FOUND.getMessage());
        }

        @Test
        void 성공하면_소셜미디어의_정보가_변경된다() {
            // given
            School 테코대학교 = schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());

            SocialMedia socialMedia = socialMediaRepository.save(SocialMediaFixture.builder()
                .ownerId(테코대학교.getId())
                .ownerType(OwnerType.SCHOOL)
                .mediaType(SocialMediaType.INSTAGRAM)
                .build());

            // when
            var command = new SocialMediaUpdateCommand(
                "테코대학교 인스타그램",
                "https://instagram.com/tecodaehak",
                "https://image.com/logo.png"
            );
            socialMediaCommandService.updateSocialMedia(socialMedia.getId(), command);

            // then
            SocialMedia actual = socialMediaRepository.getOrThrow(socialMedia.getId());
            assertSoftly(softly -> {
                softly.assertThat(actual.getName()).isEqualTo(command.name());
                softly.assertThat(actual.getUrl()).isEqualTo(command.url());
                softly.assertThat(actual.getLogoUrl()).isEqualTo(command.logoUrl());
            });
        }
    }
}
