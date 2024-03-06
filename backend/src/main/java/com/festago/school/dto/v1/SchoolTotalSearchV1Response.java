package com.festago.school.dto.v1;

import jakarta.annotation.Nullable;

public record SchoolTotalSearchV1Response(
    Long id,
    String name,
    String logoUrl,
    @Nullable SchoolSearchRecentFestivalV1Response festival
) {

}
