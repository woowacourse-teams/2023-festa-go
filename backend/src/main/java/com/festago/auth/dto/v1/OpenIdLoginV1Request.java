package com.festago.auth.dto.v1;

import com.festago.auth.domain.SocialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OpenIdLoginV1Request(
    @NotNull SocialType socialType,
    @NotBlank String idToken
) {

}
