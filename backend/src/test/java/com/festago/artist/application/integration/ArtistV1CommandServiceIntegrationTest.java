package com.festago.artist.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.admin.dto.ArtistV1CreateRequest;
import com.festago.admin.dto.ArtistV1UpdateRequest;
import com.festago.artist.application.ArtistV1CommandService;
import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.support.ApplicationIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistV1CommandServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    ArtistV1CommandService artistV1CommandService;

    @Autowired
    ArtistRepository artistRepository;

    @Test
    void 아티스트를_저장한다() {
        // given
        ArtistV1CreateRequest request = new ArtistV1CreateRequest("윤서연", "https://image.com/image.png");

        // when
        Long artistId = artistV1CommandService.save(request);

        // then
        Artist actual = artistRepository.getOrThrow(artistId);
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 아티스트_정보를_변경한다() {
        // given
        Long artistId = artistRepository.save(new Artist("고윤하", "https://image.com/image1.png")).getId();
        ArtistV1UpdateRequest request = new ArtistV1UpdateRequest("윤하", "https://image.com/image2.png");

        // when
        artistV1CommandService.update(request, artistId);

        // then
        Artist actual = artistRepository.getOrThrow(artistId);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(request);
    }

    @Test
    void 아티스트를_삭제한다() {
        // given
        Long artistId = artistRepository.save(new Artist("고윤하", "https://image.com/image.png")).getId();

        // when
        artistV1CommandService.delete(artistId);

        // then
        assertThat(artistRepository.findById(artistId)).isEmpty();
    }
}
