package com.festago.support.fixture;

import com.festago.auth.domain.RefreshToken;
import java.time.LocalDateTime;

public class RefreshTokenFixture extends BaseFixture {

    private Long memberId;
    private LocalDateTime expiredAt = LocalDateTime.now().plusWeeks(1);

    private RefreshTokenFixture() {
    }

    public static RefreshTokenFixture builder() {
        return new RefreshTokenFixture();
    }

    public RefreshTokenFixture memberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public RefreshTokenFixture expiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public RefreshToken build() {
        return new RefreshToken(memberId, expiredAt);
    }
}
