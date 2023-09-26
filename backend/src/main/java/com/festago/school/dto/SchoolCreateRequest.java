package com.festago.school.dto;

import jakarta.validation.constraints.NotNull;

public record SchoolCreateRequest(
    @NotNull(message = "name 은 null 일 수 없습니다.") String name,
    @NotNull(message = "domain 은 null 일 수 없습니다.") String domain
) {

}
