package com.festago.auth.dto.v1;

import org.hibernate.validator.constraints.UUID;

public record RefreshTokenV1Request(
    @UUID String refreshToken
) {

}
