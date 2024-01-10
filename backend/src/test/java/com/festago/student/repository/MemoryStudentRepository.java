package com.festago.student.repository;

import com.festago.member.domain.Member;
import com.festago.student.domain.Student;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryStudentRepository implements StudentRepository {

    private final Map<Long, Student> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @Override
    public boolean existsByMemberAndSchoolId(Member member, Long schoolId) {
        return memory.values().stream()
            .anyMatch(student -> student.getMember().getId().equals(member.getId()) && student.getSchool().getId()
                .equals(schoolId));
    }

    @Override
    public boolean existsByUsernameAndSchoolId(String username, Long schoolId) {
        return memory.values().stream()
            .anyMatch(
                student -> student.getUsername().equals(username) && student.getSchool().getId().equals(schoolId));
    }

    @Override
    public boolean existsByMemberId(Long memberId) {
        return memory.values().stream()
            .anyMatch(student -> student.getMember().getId().equals(memberId));
    }

    @SneakyThrows
    @Override
    public Student save(Student student) {
        Field idField = student.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(student, autoIncrement.incrementAndGet());
        memory.put(student.getId(), student);
        return student;
    }

    @Override
    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public Optional<Student> findByMemberIdWithFetch(Long memberId) {
        return memory.values().stream()
            .filter(student -> student.getMember().getId().equals(memberId))
            .findAny();
    }

    public void clear() {
        memory.clear();
    }
}
