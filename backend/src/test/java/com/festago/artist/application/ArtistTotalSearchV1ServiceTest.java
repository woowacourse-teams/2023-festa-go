package com.festago.artist.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.festago.artist.dto.ArtistSearchStageCountV1Response;
import com.festago.artist.dto.ArtistSearchV1Response;
import com.festago.artist.dto.ArtistTotalSearchV1Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ArtistTotalSearchV1ServiceTest {

    @Mock
    ArtistSearchV1QueryService artistSearchV1QueryService;

    @Mock
    ArtistSearchStageCountV1QueryService artistSearchStageCountV1QueryService;

    @Spy
    Clock clock = Clock.systemDefaultZone();

    @InjectMocks
    ArtistTotalSearchV1Service artistTotalSearchV1Service;

    @Test
    void 아티스트_정보와_공연_일정을_종합하여_반환한다() {
        List<ArtistSearchV1Response> artists = List.of(
            new ArtistSearchV1Response(1L, "아이브", "www.IVE-image.png"),
            new ArtistSearchV1Response(2L, "아이유", "www.IU-image.png"),
            new ArtistSearchV1Response(3L, "(여자)아이들", "www.IDLE-image.png"));
        given(artistSearchV1QueryService.findAllByKeyword("아이"))
            .willReturn(artists);

        LocalDate today = LocalDate.now();
        Map<Long, ArtistSearchStageCountV1Response> artistToStageSchedule = Map.of(
            1L, new ArtistSearchStageCountV1Response(1, 0),
            2L, new ArtistSearchStageCountV1Response(0, 0),
            3L, new ArtistSearchStageCountV1Response(0, 2));
        given(artistSearchStageCountV1QueryService.findArtistsStageCountAfterDateTime(
            List.of(1L, 2L, 3L), today.atStartOfDay()))
            .willReturn(artistToStageSchedule);

        // when
        List<ArtistTotalSearchV1Response> actual = artistTotalSearchV1Service.findAllByKeyword("아이");

        // then
        var expected = List.of(
            new ArtistTotalSearchV1Response(1L, "아이브", "www.IVE-image.png", 1, 0),
            new ArtistTotalSearchV1Response(2L, "아이유", "www.IU-image.png", 0, 0),
            new ArtistTotalSearchV1Response(3L, "(여자)아이들", "www.IDLE-image.png", 0, 2)
        );
        assertThat(actual).isEqualTo(expected);
    }

}
