package com.festago.school.repository.v1;

import java.time.LocalDate;

public record SchoolFestivalV1SearchCondition(
    Long lastFestivalId,
    LocalDate lastStartDate,
    Boolean isPast,
    Integer size
) {

}
