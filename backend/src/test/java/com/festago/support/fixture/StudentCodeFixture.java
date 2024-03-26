package com.festago.support.fixture;

import com.festago.member.domain.Member;
import com.festago.school.domain.School;
import com.festago.student.domain.StudentCode;
import com.festago.student.domain.VerificationCode;
import java.time.LocalDateTime;

@Deprecated
public class StudentCodeFixture {

    private Long id;
    private VerificationCode code = new VerificationCode("123456");
    private School school = SchoolFixture.builder().build();
    private Member member = MemberFixture.builder().build();
    private String username = "ash";
    private LocalDateTime issuedAt = LocalDateTime.now();

    private StudentCodeFixture() {
    }

    public static StudentCodeFixture builder() {
        return new StudentCodeFixture();
    }

    public StudentCodeFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StudentCodeFixture code(VerificationCode code) {
        this.code = code;
        return this;
    }

    public StudentCodeFixture school(School school) {
        this.school = school;
        return this;
    }

    public StudentCodeFixture member(Member member) {
        this.member = member;
        return this;
    }

    public StudentCodeFixture username(String username) {
        this.username = username;
        return this;
    }

    public StudentCodeFixture issuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    public StudentCode build() {
        return new StudentCode(id, code, school, member, username, issuedAt);
    }
}
