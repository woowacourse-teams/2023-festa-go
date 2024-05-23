package com.festago.artist.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.application.ArtistSearchV1QueryService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchV1Response;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.ArtistFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistSearchV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    ArtistSearchV1QueryService artistSearchV1QueryService;

    @Autowired
    ArtistRepository artistRepository;

    @Test
    void 검색어가_한글자면_동등검색을_한다() {
        // given
        artistRepository.save(ArtistFixture.builder().name("난못").build());
        artistRepository.save(ArtistFixture.builder().name("못난").build());
        artistRepository.save(ArtistFixture.builder().name("못").build());

        // when
        List<ArtistSearchV1Response> actual = artistSearchV1QueryService.findAllByKeyword("못");

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            softly.assertThat(actual.get(0).name()).isEqualTo("못");
        });
    }

    @Test
    void 검색어가_두글자_이상이면_like검색을_한다() {
        // given
        artistRepository.save(ArtistFixture.builder().name("에이핑크").build());
        artistRepository.save(ArtistFixture.builder().name("블랙핑크").build());
        artistRepository.save(ArtistFixture.builder().name("핑크").build());
        artistRepository.save(ArtistFixture.builder().name("핑크 플로이드").build());
        artistRepository.save(ArtistFixture.builder().name("핑").build());
        artistRepository.save(ArtistFixture.builder().name("크").build());

        // when
        List<ArtistSearchV1Response> actual = artistSearchV1QueryService.findAllByKeyword("핑크");

        // then
        assertThat(actual).hasSize(4);
    }

    @Test
    void 아티스트명은_영어_한국어_순으로_오름차순_정렬된다() {
        // given
        Artist 가_아티스트 = artistRepository.save(ArtistFixture.builder().name("가_아티스트").build());
        Artist A_아티스트 = artistRepository.save(ArtistFixture.builder().name("A_아티스트").build());
        Artist 나_아티스트 = artistRepository.save(ArtistFixture.builder().name("나_아티스트").build());
        Artist C_아티스트 = artistRepository.save(ArtistFixture.builder().name("C_아티스트").build());

        // when
        List<ArtistSearchV1Response> actual = artistSearchV1QueryService.findAllByKeyword("아티스트");

        // then
        List<Long> result = actual.stream()
            .map(ArtistSearchV1Response::id)
            .toList();
        assertThat(result).isEqualTo(List.of(A_아티스트.getId(), C_아티스트.getId(), 가_아티스트.getId(), 나_아티스트.getId()));
    }

    @Test
    void 검색결과가_10개_이상이면_예외() {
        // given
        for (int i = 0; i < 10; i++) {
            artistRepository.save(ArtistFixture.builder().name("핑크").build());
        }

        // when && then
        assertThatThrownBy(() -> artistSearchV1QueryService.findAllByKeyword("핑크"))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 검색_결과가_없다면_빈리스트_반환() {
        // when
        List<ArtistSearchV1Response> actual = artistSearchV1QueryService.findAllByKeyword("없음");

        // then
        assertThat(actual).isEmpty();
    }
}
