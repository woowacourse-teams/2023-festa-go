package com.festago.fcm.domain;

import com.festago.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class MemberFCM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @NotNull
    @ManyToOne
    private final Member member;

    @NotNull
    private final String fcmToken;

    public MemberFCM(Member member, String fcmToken) {
        this(null, member, fcmToken);
    }

    public MemberFCM(Long id, Member member, String fcmToken) {
        validate(member, fcmToken);
        this.id = id;
        this.member = member;
        this.fcmToken = fcmToken;
    }

    private void validate(Member member, String fcmToken) {
        if (member == null || fcmToken == null) {
            throw new IllegalArgumentException("MemberFCM 은 허용되지 않은 null 값으로 생성할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getFcmToken() {
        return fcmToken;
    }
}
