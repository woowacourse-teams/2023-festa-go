package com.festago.artist.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistDetailV1Response;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.support.ApplicationIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistDetailV1QueryServiceTest extends ApplicationIntegrationTest {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    @Autowired
    ArtistDetailV1QueryService artistDetailV1QueryService;

    @Test
    void 아티스트_정보를_조회할_수_있다() {
        // given
        Artist pooh = artistRepository.save(new Artist("pooh", "image.jpg"));
        Long id = pooh.getId();
        socialMediaRepository.save(makeArtistSocialMedia(id, OwnerType.ARTIST, SocialMediaType.INSTAGRAM));
        socialMediaRepository.save(makeArtistSocialMedia(id, OwnerType.ARTIST, SocialMediaType.YOUTUBE));

        // when
        ArtistDetailV1Response acutal = artistDetailV1QueryService.findArtistDetail(id);

        // then
        assertThat(acutal.socialMedias()).hasSize(2);
    }

    @Test
    void 소셜_매체가_없더라도_반환한다() {
        // given
        Artist pooh = artistRepository.save(new Artist("pooh", "image.jpg"));
        Long id = pooh.getId();

        // when
        ArtistDetailV1Response acutal = artistDetailV1QueryService.findArtistDetail(id);

        // then
        assertThat(acutal.socialMedias()).isEmpty();
    }

    @Test
    void 소셜_매체의_주인_아이디가_같더라도_주인의_타입에_따라_구분하여_반환한다() {
        // given
        Artist pooh = artistRepository.save(new Artist("pooh", "image.jpg"));
        Long id = pooh.getId();
        socialMediaRepository.save(makeArtistSocialMedia(id, OwnerType.ARTIST, SocialMediaType.INSTAGRAM));
        socialMediaRepository.save(makeArtistSocialMedia(id, OwnerType.SCHOOL, SocialMediaType.INSTAGRAM));

        // when
        ArtistDetailV1Response acutal = artistDetailV1QueryService.findArtistDetail(id);

        // then
        assertThat(acutal.socialMedias()).hasSize(1);
    }

    @Test
    void 존재하지_않는_아티스트를_검색하면_에외() {
        // given & when & then
        assertThatThrownBy(() -> artistDetailV1QueryService.findArtistDetail(1L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorCode.ARTIST_NOT_FOUND.getMessage());

    }

    SocialMedia makeArtistSocialMedia(Long id, OwnerType ownerType, SocialMediaType socialMediaType) {
        return new SocialMedia(id, ownerType, socialMediaType, "총학생회", "logoUrl", "url");
    }
}
