package com.festago.auth.dto.v1;

import java.time.LocalDateTime;

public record RefreshTokenResult(
    Long memberId,
    String token,
    LocalDateTime expiredAt
) {

}
