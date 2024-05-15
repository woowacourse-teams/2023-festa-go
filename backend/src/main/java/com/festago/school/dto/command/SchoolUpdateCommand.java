package com.festago.school.dto.command;

import com.festago.school.domain.SchoolRegion;
import lombok.Builder;

@Builder
public record SchoolUpdateCommand(
    String name,
    String domain,
    SchoolRegion region,
    String logoUrl,
    String backgroundImageUrl
) {
}
