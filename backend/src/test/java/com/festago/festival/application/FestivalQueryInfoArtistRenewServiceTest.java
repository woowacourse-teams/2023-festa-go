package com.festago.festival.application;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.reset;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.festival.domain.FestivalIdStageArtistsResolver;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.MemoryFestivalQueryInfoRepository;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.FestivalQueryInfoFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalQueryInfoArtistRenewServiceTest {

    FestivalInfoRepository festivalInfoRepository;
    FestivalIdStageArtistsResolver festivalIdStageArtistsResolver = mock();
    ArtistsSerializer artistsSerializer = artists -> artists.stream()
        .map(Artist::getName)
        .collect(joining(",")); // "뉴진스,에픽하이"
    FestivalQueryInfoArtistRenewService festivalQueryInfoArtistRenewService;

    @BeforeEach
    void setUp() {
        festivalInfoRepository = new MemoryFestivalQueryInfoRepository();
        festivalQueryInfoArtistRenewService = new FestivalQueryInfoArtistRenewService(
            festivalInfoRepository,
            festivalIdStageArtistsResolver,
            artistsSerializer
        );
        reset(festivalIdStageArtistsResolver);
    }

    @Nested
    class renewArtistInfo {

        private final Long festivalId = 1L;

        private final Artist 뉴진스 = ArtistFixture.builder().id(1L).name("뉴진스").build();
        private final Artist 소녀시대 = ArtistFixture.builder().id(2L).name("소녀시대").build();
        FestivalQueryInfo festivalQueryInfo;

        @BeforeEach
        void setUp() {
            festivalQueryInfo = festivalInfoRepository.save(FestivalQueryInfoFixture.builder().festivalId(festivalId).build());
        }

        @Test
        void 기존의_ArtistInfo가_갱신된다() {
            // given
            given(festivalIdStageArtistsResolver.resolve(anyLong()))
                .willReturn(List.of(뉴진스, 소녀시대));
            festivalQueryInfo.updateArtistInfo(List.of(뉴진스), artistsSerializer);

            // when
            festivalQueryInfoArtistRenewService.renewArtistInfo(festivalId);

            // then
            FestivalQueryInfo actual = festivalInfoRepository.findByFestivalId(festivalId).get();
            assertThat(actual.getArtistInfo()).isEqualTo("뉴진스,소녀시대");
        }
    }
}
