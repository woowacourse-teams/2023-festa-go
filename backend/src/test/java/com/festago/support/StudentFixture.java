package com.festago.support;

import com.festago.member.domain.Member;
import com.festago.school.domain.School;
import com.festago.student.domain.Student;

public class StudentFixture {

    private Long id;
    private Member member = MemberFixture.member().build();
    private School school = SchoolFixture.school().build();
    private String username = "xxeol2";

    private StudentFixture() {
    }

    public static StudentFixture student() {
        return new StudentFixture();
    }

    public StudentFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StudentFixture member(Member member) {
        this.member = member;
        return this;
    }

    public StudentFixture school(School school) {
        this.school = school;
        return this;
    }

    public StudentFixture username(String username) {
        this.username = username;
        return this;
    }

    public Student build() {
        return new Student(id, member, school, username);
    }
}
