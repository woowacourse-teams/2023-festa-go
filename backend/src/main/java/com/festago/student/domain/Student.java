package com.festago.student.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import com.festago.member.domain.Member;
import com.festago.school.domain.School;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends BaseTimeEntity {

    private static final int MAX_USERNAME_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @Size(max = MAX_USERNAME_LENGTH)
    @NotNull
    private String username;

    public Student(Member member, School school, String username) {
        this(null, member, school, username);
    }

    public Student(Long id, Member member, School school, String username) {
        validate(member, school, username);
        this.id = id;
        this.member = member;
        this.school = school;
        this.username = username;
    }

    private void validate(Member member, School school, String username) {
        validateMember(member);
        validateSchool(school);
        validateUsername(username);
    }

    private void validateMember(Member member) {
        Validator.notNull(member, "member");
    }

    private void validateSchool(School school) {
        Validator.notNull(school, "school");
    }

    private void validateUsername(String username) {
        String fieldName = "username";
        Validator.notBlank(username, fieldName);
        Validator.maxLength(username, MAX_USERNAME_LENGTH, fieldName);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public School getSchool() {
        return school;
    }

    public String getUsername() {
        return username;
    }
}
