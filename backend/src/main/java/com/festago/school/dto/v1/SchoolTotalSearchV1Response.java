package com.festago.school.dto.v1;

import jakarta.annotation.Nullable;
import java.time.LocalDate;

public record SchoolTotalSearchV1Response(
    Long id,
    String name,
    String logoUrl,
    @Nullable LocalDate upcomingFestivalStartDate
) {

}
