package com.festago.artist.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.support.fixture.ArtistFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistTest {

    @Nested
    class getImageUrls {

        @Test
        void 가지고_있는_모든_이미지_URL을_반환한다() {
            // given
            Artist artist = ArtistFixture.builder().build();

            // when
            List<String> imageUrls = artist.getImageUrls();

            // then
            assertThat(imageUrls)
                .containsExactlyInAnyOrder(artist.getProfileImage(), artist.getBackgroundImageUrl());
        }
    }
}
