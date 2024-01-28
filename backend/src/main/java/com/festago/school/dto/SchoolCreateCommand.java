package com.festago.school.dto;

import com.festago.common.util.Validator;
import com.festago.school.domain.SchoolRegion;

public record SchoolCreateCommand(
    String name,
    String domain,
    SchoolRegion region
) {

    public SchoolCreateCommand {
        Validator.notNull(name, "name");
        Validator.notNull(domain, "domain");
        Validator.notNull(region, "region");
    }
}
