package com.festago.festival.dto;

import com.festago.festival.repository.FestivalFilter;
import com.festago.school.domain.SchoolRegion;
import java.time.LocalDate;

public record FestivalV1QueryRequest(
    SchoolRegion location,
    FestivalFilter filter,
    Long lastFestivalId,
    LocalDate lastStartDate
) {

}
