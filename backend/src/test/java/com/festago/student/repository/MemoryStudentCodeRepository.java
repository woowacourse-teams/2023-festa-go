package com.festago.student.repository;

import com.festago.member.domain.Member;
import com.festago.student.domain.StudentCode;
import com.festago.student.domain.VerificationCode;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryStudentCodeRepository implements StudentCodeRepository {

    private final Map<Long, StudentCode> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @SneakyThrows
    @Override
    public StudentCode save(StudentCode studentCode) {
        Field idField = studentCode.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(studentCode, autoIncrement.incrementAndGet());
        memory.put(studentCode.getId(), studentCode);
        return studentCode;
    }

    @Override
    public void deleteByMember(Member member) {
        memory.entrySet()
            .removeIf(it -> it.getValue().getMember().getId().equals(member.getId()));
    }

    @Override
    public Optional<StudentCode> findByCodeAndMember(VerificationCode code, Member member) {
        return memory.values().stream()
            .filter(
                it -> it.getCode().getValue().equals(code.getValue()) && it.getMember().getId().equals(member.getId()))
            .findAny();
    }

    @Override
    public Optional<StudentCode> findByMemberId(Long memberId) {
        return memory.values().stream()
            .filter(it -> it.getMember().getId().equals(memberId))
            .findAny();
    }

    public void clear() {
        memory.clear();
    }
}
