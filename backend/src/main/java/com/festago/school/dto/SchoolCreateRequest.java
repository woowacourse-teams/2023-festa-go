package com.festago.school.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @deprecated API 버저닝이 적용되면 해당 클래스 삭제
 */
@Deprecated(forRemoval = true)
public record SchoolCreateRequest(
    @NotBlank(message = "domain은 공백일 수 없습니다.")
    String domain,
    @NotBlank(message = "name은 공백일 수 없습니다.")
    String name
) {

}
