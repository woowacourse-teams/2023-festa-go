package com.festago.school.dto.command;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import lombok.Builder;

@Builder
public record SchoolCreateCommand(
    String name,
    String domain,
    SchoolRegion region,
    String logoUrl,
    String backgroundImageUrl
) {

    public School toEntity() {
        return new School(
            null,
            domain,
            name,
            logoUrl,
            backgroundImageUrl,
            region
        );
    }
}
