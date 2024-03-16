package com.festago.school.repository.v1;

import java.time.LocalDate;
import org.springframework.data.domain.Pageable;

public record SchoolFestivalV1SearchCondition(
    Long lastFestivalId,
    LocalDate lastStartDate,
    Boolean isPast,
    Pageable pageable
) {

}
