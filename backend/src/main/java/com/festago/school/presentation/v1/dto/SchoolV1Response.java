package com.festago.school.presentation.v1.dto;

import com.festago.school.domain.SchoolRegion;

public record SchoolV1Response(
    Long id,
    String domain,
    String name,
    SchoolRegion region
) {

}
