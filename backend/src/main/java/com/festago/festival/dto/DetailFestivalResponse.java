package com.festago.festival.dto;

import com.festago.festival.domain.Festival;
import com.festago.stage.domain.Stage;
import com.festago.stage.dto.DetailStageResponse;
import java.time.LocalDate;
import java.util.List;

public record DetailFestivalResponse(
    Long id,
    Long schoolId,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail,
    List<DetailStageResponse> stages) {

    public static DetailFestivalResponse of(Festival festival, List<Stage> stages) {
        List<DetailStageResponse> stageResponses = stages.stream()
            .map(DetailStageResponse::from)
            .toList();
        return new DetailFestivalResponse(
            festival.getId(),
            festival.getSchool().getId(),
            festival.getName(),
            festival.getStartDate(),
            festival.getEndDate(),
            festival.getThumbnail(),
            stageResponses
        );
    }
}
