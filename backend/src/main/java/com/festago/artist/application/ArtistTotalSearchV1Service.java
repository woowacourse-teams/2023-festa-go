package com.festago.artist.application;

import com.festago.artist.dto.ArtistSearchStageCountV1Response;
import com.festago.artist.dto.ArtistSearchTotalV1Response;
import com.festago.artist.dto.ArtistSearchV1Response;
import com.festago.artist.repository.ArtistV1SearchQueryDslRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final ArtistV1SearchQueryDslRepository artistV1SearchQueryDslRepository;

    public List<ArtistSearchTotalV1Response> findAllByKeyword(String keyword, LocalDate today) {
        List<ArtistSearchV1Response> artists = artistSearchV1QueryService.findAllByKeyword(keyword);
        List<Long> artistIds = artists.stream()
            .map(ArtistSearchV1Response::id)
            .toList();
        Map<Long, ArtistSearchStageCountV1Response> artistToStageCount = artistV1SearchQueryDslRepository.findArtistsStageScheduleAfterDateTime(
            artistIds, LocalDateTime.of(today, LocalTime.MIN));
        return artists.stream()
            .map(it -> ArtistSearchTotalV1Response.of(it, artistToStageCount.get(it.id())))
            .toList();
    }
}
