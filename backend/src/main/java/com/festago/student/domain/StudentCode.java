package com.festago.student.domain;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.member.domain.Member;
import com.festago.school.domain.School;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.StringUtils;

@Entity
public class StudentCode {

    private static final int MIN_REQUEST_TERM_SECONDS = 30;

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

    private String username;

    @CreatedDate
    private LocalDateTime issuedAt;

    protected StudentCode() {
    }

    public StudentCode(VerificationCode code, School school, Member member, String username) {
        this(null, code, school, member, username, null);
    }

    public StudentCode(Long id, VerificationCode code, School school, Member member, String username,
                       LocalDateTime issuedAt) {
        validate(username);
        this.id = id;
        this.code = code;
        this.school = school;
        this.member = member;
        this.username = username;
        this.issuedAt = issuedAt;
    }

    private void validate(String username) {
        if (!StringUtils.hasText(username)) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        if (username.length() > 255) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean canReissue(LocalDateTime currentTime) {
        return SECONDS.between(issuedAt, currentTime) > MIN_REQUEST_TERM_SECONDS;
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
