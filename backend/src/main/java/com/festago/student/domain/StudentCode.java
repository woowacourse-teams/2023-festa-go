package com.festago.student.domain;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.festago.common.util.Validator;
import com.festago.member.domain.Member;
import com.festago.school.domain.School;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentCode {

    private static final int MIN_REQUEST_TERM_SECONDS = 30;
    private static final int MAX_USERNAME_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private VerificationCode code;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Member member;

    @NotNull
    @Size(max = MAX_USERNAME_LENGTH)
    private String username;

    @NotNull
    @LastModifiedDate
    private LocalDateTime issuedAt;

    public StudentCode(VerificationCode code, School school, Member member, String username) {
        this(null, code, school, member, username, null);
    }

    public StudentCode(Long id, VerificationCode code, School school, Member member, String username,
                       LocalDateTime issuedAt) {
        validate(code, school, member, username);
        this.id = id;
        this.code = code;
        this.school = school;
        this.member = member;
        this.username = username;
        this.issuedAt = issuedAt;
    }

    private void validate(VerificationCode code, School school, Member member, String username) {
        validateVerificationCode(code);
        validateSchool(school);
        validateMember(member);
        validateUsername(username);
    }

    private void validateVerificationCode(VerificationCode code) {
        Validator.notNull(code, "validateVerificationCode");
    }

    private void validateSchool(School school) {
        Validator.notNull(school, "school");
    }

    private void validateMember(Member member) {
        Validator.notNull(member, "member");
    }

    private void validateUsername(String username) {
        String fieldName = "username";
        Validator.hasBlank(username, fieldName);
        Validator.maxLength(username, MAX_USERNAME_LENGTH, fieldName);
    }

    public boolean canReissue(LocalDateTime currentTime) {
        return SECONDS.between(issuedAt, currentTime) > MIN_REQUEST_TERM_SECONDS;
    }

    public void reissue(VerificationCode code, School school, String username) {
        this.code = code;
        this.school = school;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
}
