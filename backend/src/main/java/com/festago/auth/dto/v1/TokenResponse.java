package com.festago.auth.dto.v1;

import java.time.LocalDateTime;

public record TokenResponse(
    String token,
    LocalDateTime expiredAt
) {

}
