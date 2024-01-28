package com.festago.festival.dto;

import com.festago.festival.repository.FestivalFilter;
import com.festago.school.domain.SchoolRegion;
import java.time.LocalDate;

public record FestivalV1QueryRequest(
    SchoolRegion location,
    FestivalFilter filter,
    Integer limit,
    Long lastFestivalId,
    LocalDate lastStartDate
) {

    public FestivalV1QueryRequest(SchoolRegion location, FestivalFilter filter, Integer limit, Long lastFestivalId,
                                  LocalDate lastStartDate) {
        this.location = location;
        this.filter = filter;
        this.limit = limit == null ? 10 : limit;
        this.lastFestivalId = lastFestivalId;
        this.lastStartDate = lastStartDate;
    }
}
