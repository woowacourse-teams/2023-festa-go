package com.festago.festival.domain;

import static org.assertj.core.api.Assertions.*;

import com.festago.artist.domain.Artist;
import com.festago.artist.infrastructure.DelimiterArtistsSerializer;
import com.festago.support.fixture.ArtistFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalQueryInfoTest {

    @Nested
    class updateArtistInfo {

        @Test
        void 직렬화된_아티스트_정보는_중복이_없고_아티스트_식별자_오름차순_정렬된다() {
            // given
            FestivalQueryInfo festivalQueryInfo = FestivalQueryInfo.create(1L);
            DelimiterArtistsSerializer serializer = new DelimiterArtistsSerializer(",");
            List<Artist> artists = List.of(
                ArtistFixture.builder().id(3L).name("아이유").build(),
                ArtistFixture.builder().id(2L).name("에픽하이").build(),
                ArtistFixture.builder().id(2L).name("에픽하이").build(),
                ArtistFixture.builder().id(1L).name("SG워너비").build()
            );

            // when
            festivalQueryInfo.updateArtistInfo(artists, serializer);

            // then
            assertThat(festivalQueryInfo.getArtistInfo()).isEqualTo("SG워너비,에픽하이,아이유");
        }
    }
}
