package com.festago.dto;

import com.festago.domain.Festival;
import java.time.LocalDate;
import java.util.List;

public record FestivalDetailResponse(Long id,
                                     String name,
                                     LocalDate startDate,
                                     LocalDate endDate,
                                     String thumbnail,
                                     List<FestivalDetailStageResponse> stages) {

    public static FestivalDetailResponse from(Festival festival) {
        List<FestivalDetailStageResponse> stages = festival.getStages().stream()
            .map(FestivalDetailStageResponse::from)
            .toList();
        return new FestivalDetailResponse(
            festival.getId(),
            festival.getName(),
            festival.getStartDate(),
            festival.getEndDate(),
            festival.getThumbnail(),
            stages
        );
    }
}
