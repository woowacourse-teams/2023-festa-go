package com.festago.fcm.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "member_fcm")
public class MemberFCM extends BaseTimeEntity {

    private static final int MAX_FCM_TOKEN_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long memberId;

    @NotNull
    @Size(max = MAX_FCM_TOKEN_LENGTH)
    private String fcmToken;

    protected MemberFCM() {
    }

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
        validateMemberId(memberId);
        validateFcmToken(fcmToken);
    }

    private void validateMemberId(Long memberId) {
        Validator.notNull(memberId, "memberId");
    }

    private void validateFcmToken(String fcmToken) {
        String fieldName = "fcmToken";
        Validator.notBlank(fcmToken, fieldName);
        Validator.maxLength(fcmToken, MAX_FCM_TOKEN_LENGTH, fieldName);
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
