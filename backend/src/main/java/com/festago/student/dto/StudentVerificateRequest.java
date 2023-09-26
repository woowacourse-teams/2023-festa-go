package com.festago.student.dto;

import jakarta.validation.constraints.NotBlank;

public record StudentVerificateRequest(
    @NotBlank(message = "code는 공백일 수 없습니다.") String code
) {

}
