package com.festago.festival.repository;

import com.festago.school.domain.SchoolRegion;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;

public record FestivalSearchCondition(
    FestivalFilter filter,
    SchoolRegion region,
    LocalDate lastStartDate,
    Long lastFestivalId,
    Pageable page,
    LocalDate currentTime
) {

}
