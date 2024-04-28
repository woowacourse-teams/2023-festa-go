package com.festago.auth.dto.v1;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public record LogoutV1Request(
    @NotNull @UUID String refreshToken
) {

}
