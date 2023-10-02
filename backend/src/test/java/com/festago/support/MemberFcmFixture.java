package com.festago.support;

import com.festago.fcm.domain.MemberFCM;
import java.util.UUID;

public class MemberFcmFixture {

    private Long id;
    private Long memberId = 1L;
    private String fcmToken = UUID.randomUUID().toString();

    public static MemberFcmFixture memberFcm() {
        return new MemberFcmFixture();
    }

    public MemberFcmFixture id(Long id) {
        this.id = id;
        return this;
    }

    public MemberFcmFixture memberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public MemberFcmFixture fcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    public MemberFCM build() {
        return new MemberFCM(id, memberId, fcmToken);
    }
}
