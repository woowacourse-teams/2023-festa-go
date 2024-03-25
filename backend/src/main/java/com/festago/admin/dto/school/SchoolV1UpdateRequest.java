package com.festago.admin.dto.school;

import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolUpdateCommand;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SchoolV1UpdateRequest(
    @NotBlank
    String name,
    @NotBlank
    String domain,
    @NotNull
    SchoolRegion region,
    @Nullable
    String logoUrl,
    @Nullable
    String backgroundImageUrl
) {

    public SchoolUpdateCommand toCommand() {
        return SchoolUpdateCommand.builder()
            .name(name)
            .domain(domain)
            .region(region)
            .logoUrl(logoUrl)
            .backgroundImageUrl(backgroundImageUrl)
            .build();
    }
}
