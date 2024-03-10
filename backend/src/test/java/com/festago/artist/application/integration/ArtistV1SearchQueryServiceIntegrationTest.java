package com.festago.artist.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.application.ArtistV1SearchQueryService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchTotalResponse;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.support.ApplicationIntegrationTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistV1SearchQueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    ArtistV1SearchQueryService artistV1SearchQueryService;

    @Autowired
    ArtistRepository artistRepository;

    @Test
    void 검색어가_한글자면_동등검색을_한다() {
        // given
        artistRepository.save(new Artist("난못", "www.profileImage.png"));
        artistRepository.save(new Artist("못난", "www.profileImage.png"));
        artistRepository.save(new Artist("못", "www.profileImage.png"));

        // when
        List<ArtistSearchTotalResponse> actual = artistV1SearchQueryService.findAllByKeyword("못", LocalDate.now());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            softly.assertThat(actual.get(0).name()).isEqualTo("못");
        });
    }

    @Test
    void 검색어가_두글자_이상이면_like검색을_한다() {
        // given
        artistRepository.save(new Artist("에이핑크", "www.profileImage.png"));
        artistRepository.save(new Artist("블랙핑크", "www.profileImage.png"));
        artistRepository.save(new Artist("핑크", "www.profileImage.png"));
        artistRepository.save(new Artist("핑크 플로이드", "www.profileImage.png"));
        artistRepository.save(new Artist("핑", "www.profileImage.png"));
        artistRepository.save(new Artist("크", "www.profileImage.png"));

        // when
        List<ArtistSearchTotalResponse> actual = artistV1SearchQueryService.findAllByKeyword("핑크",LocalDate.now());

        // then
        assertThat(actual).hasSize(4);
    }

    @Test
    void 검색결과가_10개_이상이면_예외() {
        // given
        for (int i = 0; i < 10; i++) {
            artistRepository.save(new Artist("핑크", "www.profileImage.png"));
        }

        // when && then
        assertThatThrownBy(() -> artistV1SearchQueryService.findAllByKeyword("핑크",LocalDate.now()))
            .isInstanceOf(BadRequestException.class);
    }
}
