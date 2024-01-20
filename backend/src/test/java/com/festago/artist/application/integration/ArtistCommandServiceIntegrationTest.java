package com.festago.artist.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistCreateRequest;
import com.festago.artist.dto.ArtistUpdateRequest;
import com.festago.artist.repository.ArtistRepository;
import com.festago.support.ApplicationIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistCommandServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    ArtistCommandService artistCommandService;

    @Autowired
    ArtistRepository artistRepository;

    @Test
    void 아티스트를_저장한다() {
        // given
        ArtistCreateRequest request = new ArtistCreateRequest("윤하", "www.zonye.com");

        // when
        Long artistId = artistCommandService.save(request);

        // then
        Artist actual = artistRepository.getOrThrow(artistId);
        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(request)
        );
    }

    @Test
    void 아티스트_정보를_변경한다() {
        // given
        Long artistId = artistRepository.save(new Artist("고윤하", "www.naver.com")).getId();
        ArtistUpdateRequest request = new ArtistUpdateRequest("윤하", "www.c9.com");

        // when
        artistCommandService.update(request, artistId);

        // then
        Artist actual = artistRepository.getOrThrow(artistId);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(request);
    }

    @Test
    void 아티스트를_삭제한다() {
        // given
        Long artistId = artistRepository.save(new Artist("고윤하", "www.naver.com")).getId();

        // when
        artistCommandService.delete(artistId);

        // then
        assertThat(artistRepository.findById(artistId)).isEmpty();
    }
}
