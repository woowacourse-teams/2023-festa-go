package com.festago.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentSendMailRequest(
    @NotBlank(message = "username은 공백일 수 없습니다.")
    String username,
    @NotNull(message = "schoolId는 null 일 수 없습니다.")
    Long schoolId
) {

}
