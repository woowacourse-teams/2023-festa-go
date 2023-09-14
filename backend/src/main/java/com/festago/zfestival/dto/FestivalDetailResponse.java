package com.festago.zfestival.dto;

import com.festago.stage.domain.Stage;
import com.festago.zfestival.domain.Festival;
import java.time.LocalDate;
import java.util.List;

public record FestivalDetailResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail,
    List<FestivalDetailStageResponse> stages) {

    public static FestivalDetailResponse of(Festival festival, List<Stage> stages) {
        List<FestivalDetailStageResponse> stageResponses = stages.stream()
            .map(FestivalDetailStageResponse::from)
            .toList();
        return new FestivalDetailResponse(
            festival.getId(),
            festival.getName(),
            festival.getStartDate(),
            festival.getEndDate(),
            festival.getThumbnail(),
            stageResponses
        );
    }
}
