package com.festago.school.dto;

import jakarta.validation.constraints.NotBlank;

public record SchoolCreateRequest(
    @NotBlank(message = "domain은 공백일 수 없습니다.")
    String domain,
    @NotBlank(message = "name은 공백일 수 없습니다.")
    String name
) {
}
