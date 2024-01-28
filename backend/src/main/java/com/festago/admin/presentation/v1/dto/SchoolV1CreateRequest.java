package com.festago.admin.presentation.v1.dto;

import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SchoolV1CreateRequest(
    @NotBlank(message = "name은 공백일 수 없습니다.")
    String name,
    @NotBlank(message = "domain은 공백일 수 없습니다.")
    String domain,
    @NotNull(message = "region은 null일 수 없습니다.")
    SchoolRegion region
) {

    public SchoolCreateCommand toCommand() {
        return new SchoolCreateCommand(name, domain, region);
    }
}
