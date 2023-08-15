package com.festago.auth.dto;

import com.festago.auth.domain.SocialType;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull SocialType socialType, @NotNull String accessToken) {

}
