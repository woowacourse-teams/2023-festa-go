package com.festago.admin.presentation.v1.dto;

import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolUpdateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SchoolV1UpdateRequest(
    @NotBlank(message = "name은 공백일 수 없습니다.")
    String name,
    @NotBlank(message = "domain은 공백일 수 없습니다.")
    String domain,
    @NotNull(message = "region은 null일 수 없습니다.")
    SchoolRegion region
) {

    public SchoolUpdateCommand toCommand() {
        return new SchoolUpdateCommand(name, domain, region);
    }
}
