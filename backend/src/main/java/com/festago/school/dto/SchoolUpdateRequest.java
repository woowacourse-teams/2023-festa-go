package com.festago.school.dto;

import jakarta.validation.constraints.NotNull;

public record SchoolUpdateRequest(
    @NotNull(message = "domain 은 null 일 수 없습니다.") String domain,
    @NotNull(message = "name 은 null 일 수 없습니다.") String name
) {

}
