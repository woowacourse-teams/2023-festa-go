package com.festago.support.fixture;

import com.festago.fcm.domain.MemberFCM;
import java.time.LocalDateTime;
import java.util.UUID;

public class MemberFCMFixture extends BaseFixture {

    private Long id;
    private Long memberId;
    private String fcmToken;
    private LocalDateTime expiredAt;

    private MemberFCMFixture() {

    }

    public static MemberFCMFixture builder() {
        return new MemberFCMFixture();
    }

    public MemberFCMFixture id(Long id) {
        this.id = id;
        return this;
    }

    public MemberFCMFixture memberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public MemberFCMFixture fcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    public MemberFCMFixture expiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public MemberFCM build() {
        if (fcmToken == null) {
            fcmToken = UUID.randomUUID().toString();
        }
        if (expiredAt == null) {
            expiredAt = LocalDateTime.now();
        }
        return new MemberFCM(id, memberId, fcmToken, expiredAt);
    }
}
