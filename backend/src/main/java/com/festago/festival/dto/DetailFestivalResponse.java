package com.festago.festival.dto;

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

}
