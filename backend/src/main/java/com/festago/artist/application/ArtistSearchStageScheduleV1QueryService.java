package com.festago.artist.application;

import com.festago.artist.dto.ArtistSearchStageCountV1Response;
import com.festago.artist.repository.ArtistV1SearchQueryDslRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistSearchStageScheduleV1QueryService {

    private final ArtistV1SearchQueryDslRepository artistV1SearchQueryDslRepository;

    public Map<Long, ArtistSearchStageCountV1Response> findArtistsStageCountAfterDateTime(List<Long> artistIds,
                                                                                          LocalDateTime dateTime) {
        Map<Long, List<LocalDateTime>> artistToStageStartTimes = artistV1SearchQueryDslRepository.findArtistsStageScheduleAfterDateTime(
            artistIds, dateTime);
        return getStageCountResponse(artistIds, dateTime.toLocalDate(), artistToStageStartTimes);
    }

    private Map<Long, ArtistSearchStageCountV1Response> getStageCountResponse(List<Long> artistIds,
                                                                              LocalDate today,
                                                                              Map<Long, List<LocalDateTime>> artistToStageStartTimes) {
        Map<Long, ArtistSearchStageCountV1Response> result = new HashMap<>();
        for (Long artistId : artistIds) {
            int todayStageCount = 0;
            int plannedStageCount = 0;
            if (artistToStageStartTimes.containsKey(artistId)) {
                for (LocalDateTime startTime : artistToStageStartTimes.get(artistId)) {
                    if (startTime.toLocalDate().equals(today)) {
                        todayStageCount++;
                    } else {
                        plannedStageCount++;
                    }
                }
            }
            result.put(artistId, new ArtistSearchStageCountV1Response(todayStageCount, plannedStageCount));
        }
        return result;
    }
}
