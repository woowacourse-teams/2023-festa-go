package com.festago.fcm.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "member_fcm",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "member_id",
                "fcm_token"
            }
        )
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFCM extends BaseTimeEntity {

    private static final int MAX_FCM_TOKEN_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "member_id")
    private Long memberId;

    @NotNull
    @Size(max = MAX_FCM_TOKEN_LENGTH)
    @Column(name = "fcm_token")
    private String fcmToken;

    private LocalDateTime expiredAt;

    public MemberFCM(Long memberId, String fcmToken, LocalDateTime expiredAt) {
        this(null, memberId, fcmToken, expiredAt);
    }

    public MemberFCM(Long id, Long memberId, String fcmToken, LocalDateTime expiredAt) {
        validate(memberId, fcmToken, expiredAt);
        this.id = id;
        this.memberId = memberId;
        this.fcmToken = fcmToken;
        this.expiredAt = expiredAt;
    }

    private void validate(Long memberId, String fcmToken, LocalDateTime expiredAt) {
        validateMemberId(memberId);
        validateFcmToken(fcmToken);
        validateExpiredAt(expiredAt);
    }

    private void validateMemberId(Long memberId) {
        Validator.notNull(memberId, "memberId");
    }

    private void validateFcmToken(String fcmToken) {
        String fieldName = "fcmToken";
        Validator.notBlank(fcmToken, fieldName);
        Validator.maxLength(fcmToken, MAX_FCM_TOKEN_LENGTH, fieldName);
    }

    private void validateExpiredAt(LocalDateTime expiredAt) {
        Validator.notNull(expiredAt, "expiredAt");
    }

    public void changeExpiredAt(LocalDateTime expiredAt) {
        validateExpiredAt(expiredAt);
        this.expiredAt = expiredAt;
    }

    public boolean isSameToken(String fcmToken) {
        return Objects.equals(this.fcmToken, fcmToken);
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

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
