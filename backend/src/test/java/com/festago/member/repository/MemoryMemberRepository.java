package com.festago.member.repository;

import com.festago.auth.domain.SocialType;
import com.festago.member.domain.Member;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @SneakyThrows
    @Override
    public Member save(Member member) {
        Field idField = member.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, autoIncrement.incrementAndGet());
        memory.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType) {
        return memory.values().stream()
            .filter(member -> member.getSocialId().equals(socialId) && member.getSocialType().equals(socialType))
            .findAny();
    }

    @Override
    public void delete(Member member) {
        memory.remove(member.getId());
    }

    @Override
    public long count() {
        return memory.size();
    }

    public void clear() {
        memory.clear();
    }
}
