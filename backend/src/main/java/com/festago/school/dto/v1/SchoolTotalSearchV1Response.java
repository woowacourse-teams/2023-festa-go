package com.festago.school.dto.v1;

import jakarta.annotation.Nullable;
import java.time.LocalDate;

public record SchoolTotalSearchV1Response(
    Long id,
    String name,
    String logoUrl,
    @Nullable LocalDate upcomingFestivalStartDate
) {

    public static SchoolTotalSearchV1Response of(
        SchoolSearchV1Response schoolSearchV1Response,
        SchoolSearchUpcomingFestivalV1Response schoolSearchUpcomingFestivalV1Response
    ) {
        return new SchoolTotalSearchV1Response(
            schoolSearchV1Response.id(),
            schoolSearchV1Response.name(),
            schoolSearchV1Response.logoUrl(),
            schoolSearchUpcomingFestivalV1Response.startDate()
        );
    }
}
