package com.festago.artist.application;

import static java.util.stream.Collectors.toMap;

import com.festago.artist.dto.ArtistSearchStageCountV1Response;
import com.festago.artist.repository.ArtistSearchV1QueryDslRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistSearchStageCountV1QueryService {

    private final ArtistSearchV1QueryDslRepository artistSearchV1QueryDslRepository;

    public Map<Long, ArtistSearchStageCountV1Response> findArtistsStageCountAfterDateTime(
        List<Long> artistIds,
        LocalDateTime dateTime
    ) {
        Map<Long, List<LocalDateTime>> artistToStageStartTimes = artistSearchV1QueryDslRepository.findArtistsStageScheduleAfterDateTime(
            artistIds, dateTime);
        LocalDate today = dateTime.toLocalDate();
        return artistIds.stream()
            .collect(toMap(
                Function.identity(),
                artistId -> getArtistStageCount(artistId, artistToStageStartTimes, today)
            ));
    }

    private ArtistSearchStageCountV1Response getArtistStageCount(
        Long artistId,
        Map<Long, List<LocalDateTime>> artistToStageStartTimes,
        LocalDate today
    ) {
        List<LocalDateTime> stageStartTimes = artistToStageStartTimes.getOrDefault(artistId, Collections.emptyList());
        int countOfTodayStage = (int) stageStartTimes.stream()
            .filter(it -> it.toLocalDate().equals(today))
            .count();
        int countOfPlannedStage = stageStartTimes.size() - countOfTodayStage;
        return new ArtistSearchStageCountV1Response(countOfTodayStage, countOfPlannedStage);
    }
}
