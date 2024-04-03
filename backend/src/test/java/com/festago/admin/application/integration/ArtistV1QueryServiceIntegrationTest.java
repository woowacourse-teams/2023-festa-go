package com.festago.admin.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.admin.application.AdminArtistV1QueryService;
import com.festago.admin.dto.artist.AdminArtistV1Response;
import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.ArtistFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    AdminArtistV1QueryService adminArtistV1QueryService;

    @Autowired
    ArtistRepository artistRepository;

    @Test
    void 아티스트를_단건_조회한다() {
        // given
        Artist expected = artistRepository.save(ArtistFixture.builder().build());

        // when
        AdminArtistV1Response actual = adminArtistV1QueryService.findById(expected.getId());

        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void 아티스트_정보를_변경한다() {
        // given
        List<Artist> expected = List.of(
            artistRepository.save(ArtistFixture.builder().name("윤하").build()),
            artistRepository.save(ArtistFixture.builder().name("Lana del rey").build()),
            artistRepository.save(ArtistFixture.builder().name("악동뮤지션").build())
        );

        // when
        List<AdminArtistV1Response> actual = adminArtistV1QueryService.findAll();

        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
