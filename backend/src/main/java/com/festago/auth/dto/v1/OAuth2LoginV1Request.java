package com.festago.auth.dto.v1;

import jakarta.validation.constraints.NotBlank;

public record OAuth2LoginV1Request(
    @NotBlank String token
) {

}
