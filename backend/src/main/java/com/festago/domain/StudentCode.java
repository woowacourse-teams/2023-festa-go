package com.festago.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class StudentCode extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private VerificationCode code;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    protected StudentCode() {
    }

    public StudentCode(VerificationCode code, Member member, School school) {
        this(null, code, member, school);
    }

    public StudentCode(Long id, VerificationCode code, Member member, School school) {
        this.id = id;
        this.code = code;
        this.member = member;
        this.school = school;
    }

    public Long getId() {
        return id;
    }

    public VerificationCode getCode() {
        return code;
    }

    public School getSchool() {
        return school;
    }

    public Member getMember() {
        return member;
    }
}
