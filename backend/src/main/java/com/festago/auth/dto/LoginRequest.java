package com.festago.auth.dto;

import com.festago.auth.domain.SocialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
    @NotNull(message = "socialType 은 null 일 수 없습니다.") SocialType socialType,
    @NotBlank(message = "acessToken 은 공백일 수 없습니다.") String accessToken) {

}
