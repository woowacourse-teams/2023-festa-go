package com.festago.auth.dto.v1;

import com.festago.member.domain.SocialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OAuth2LoginV1Request(
    @NotNull SocialType socialType,
    @NotBlank String code
) {

}
