package com.festago.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.admin.dto.artist.ArtistV1Response;
import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.support.ApplicationIntegrationTest;
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
        Artist expected = artistRepository.save(new Artist("윤하", "www.naver.com"));

        // when
        ArtistV1Response actual = adminArtistV1QueryService.findById(expected.getId());

        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void 아티스트_정보를_변경한다() {
        // given
        List<Artist> expected = List.of(
            artistRepository.save(new Artist("윤하", "www.naver.com")),
            artistRepository.save(new Artist("Lana Del Rey", "www.kakao.com")),
            artistRepository.save(new Artist("윤종신", "www.daum.com"))
        );

        // when
        List<ArtistV1Response> actual = adminArtistV1QueryService.findAll();

        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
