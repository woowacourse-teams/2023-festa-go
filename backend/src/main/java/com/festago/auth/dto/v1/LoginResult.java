package com.festago.auth.dto.v1;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginResult(
    Long memberId,
    String nickname,
    String profileImageUrl,
    UUID refreshToken,
    LocalDateTime refreshTokenExpiredAt
) {

}
