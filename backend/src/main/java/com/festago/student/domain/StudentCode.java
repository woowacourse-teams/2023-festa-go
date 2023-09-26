package com.festago.student.domain;

import com.festago.common.domain.BaseTimeEntity;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.springframework.util.StringUtils;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_username__school",
            columnNames = {"username", "school_id"}
        )
    }
)
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

    private String username;

    protected StudentCode() {
    }

    public StudentCode(VerificationCode code, School school, Member member, String username) {
        this(null, code, school, member, username);
    }

    public StudentCode(Long id, VerificationCode code, School school, Member member, String username) {
        validate(username);
        this.id = id;
        this.code = code;
        this.school = school;
        this.member = member;
        this.username = username;
    }

    private void validate(String username) {
        if (!StringUtils.hasText(username)) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        if (username.length() > 255) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
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
}
