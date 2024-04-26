package com.festago.auth.dto.v1;

import java.time.LocalDateTime;

public record TokenRefreshResult(
    Long memberId,
    String token,
    LocalDateTime expiredAt
) {

}
