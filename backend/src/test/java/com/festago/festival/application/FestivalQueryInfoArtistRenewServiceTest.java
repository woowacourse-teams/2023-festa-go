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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalQueryInfoArtistRenewServiceTest {

    private static final String PROFILE_IMAGE_URL = "https://image.com/profileImage.png";
    private static final String BACKGROUND_IMAGE_URL = "https://image.com/backgroundImage.png";

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
        private final Artist 뉴진스 = new Artist(1L, "뉴진스", PROFILE_IMAGE_URL, BACKGROUND_IMAGE_URL);
        private final Artist 소녀시대 = new Artist(2L, "소녀시대", PROFILE_IMAGE_URL, BACKGROUND_IMAGE_URL);
        FestivalQueryInfo festivalQueryInfo;

        @BeforeEach
        void setUp() {
            festivalQueryInfo = festivalInfoRepository.save(FestivalQueryInfo.create(festivalId));
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
