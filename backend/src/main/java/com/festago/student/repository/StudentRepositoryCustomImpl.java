package com.festago.student.repository;

import static com.festago.school.domain.QSchool.school;
import static com.festago.student.domain.QStudent.student;

import com.festago.student.domain.Student;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentRepositoryCustomImpl implements StudentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Student> findByMemberIdWithFetch(Long memberId) {
        return Optional.ofNullable(queryFactory.selectFrom(student)
                .innerJoin(student.school, school).fetchJoin()
                .where(student.member.id.eq(memberId))
                .fetchOne());
    }
}
