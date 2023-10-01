package com.festago.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record StaffLoginRequest(
    @NotBlank(message = "code는 공백일 수 없습니다.")
    String code
) {

}
