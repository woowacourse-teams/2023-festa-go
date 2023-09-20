package com.festago.fcm.domain;

import com.festago.common.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "member_fcm")
public class MemberFCM extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @NotNull
    private final Long memberId;

    @NotNull
    private final String fcmToken;

    public MemberFCM(Long memberId, String fcmToken) {
        this(null, memberId, fcmToken);
    }

    public MemberFCM(Long id, Long memberId, String fcmToken) {
        validate(memberId, fcmToken);
        this.id = id;
        this.memberId = memberId;
        this.fcmToken = fcmToken;
    }

    private void validate(Long memberId, String fcmToken) {
        if (memberId == null || fcmToken == null) {
            throw new IllegalArgumentException("MemberFCM 은 허용되지 않은 null 값으로 생성할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getFcmToken() {
        return fcmToken;
    }
}
