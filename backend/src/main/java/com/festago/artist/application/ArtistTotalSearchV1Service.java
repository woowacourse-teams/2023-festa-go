package com.festago.artist.application;

import com.festago.artist.dto.ArtistSearchStageCountV1Response;
import com.festago.artist.dto.ArtistSearchV1Response;
import com.festago.artist.dto.ArtistTotalSearchV1Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistTotalSearchV1Service {

    private final ArtistSearchV1QueryService artistSearchV1QueryService;
    private final ArtistSearchStageCountV1QueryService artistSearchStageCountV1QueryService;
    private final Clock clock;

    public List<ArtistTotalSearchV1Response> findAllByKeyword(String keyword) {
        List<ArtistSearchV1Response> artists = artistSearchV1QueryService.findAllByKeyword(keyword);
        List<Long> artistIds = artists.stream()
            .map(ArtistSearchV1Response::id)
            .toList();
        Map<Long, ArtistSearchStageCountV1Response> artistToStageCount = artistSearchStageCountV1QueryService.findArtistsStageCountAfterDateTime(
            artistIds, LocalDate.now(clock).atStartOfDay());
        return artists.stream()
            .map(it -> ArtistTotalSearchV1Response.of(it, artistToStageCount.get(it.id())))
            .toList();
    }
}
