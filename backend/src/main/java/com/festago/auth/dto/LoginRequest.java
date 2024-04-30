package com.festago.auth.dto;

import com.festago.auth.domain.SocialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Deprecated(forRemoval = true)
public record LoginRequest(
    @NotNull
    SocialType socialType,
    @NotBlank
    String accessToken
) {

}
