package com.festago.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RootAdminInitializeRequest(
    @NotBlank(message = "password는 공백일 수 없습니다.")
    String password
) {

}
