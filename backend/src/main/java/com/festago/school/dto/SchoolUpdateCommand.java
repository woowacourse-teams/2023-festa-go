package com.festago.school.dto;

import com.festago.common.util.Validator;
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

    public SchoolUpdateCommand {
        Validator.notNull(name, "name");
        Validator.notNull(domain, "domain");
        Validator.notNull(region, "region");
    }
}
