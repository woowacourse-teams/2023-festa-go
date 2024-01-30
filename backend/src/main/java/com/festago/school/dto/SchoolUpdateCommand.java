package com.festago.school.dto;

import com.festago.common.util.Validator;
import com.festago.school.domain.SchoolRegion;

public record SchoolUpdateCommand(
    String name,
    String domain,
    SchoolRegion region
) {

    public SchoolUpdateCommand {
        Validator.notNull(name, "name");
        Validator.notNull(domain, "domain");
        Validator.notNull(region, "region");
    }
}
