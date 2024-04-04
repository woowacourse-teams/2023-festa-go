package com.festago.member.repository;

import com.festago.auth.domain.SocialType;
import com.festago.member.domain.Member;
import com.festago.support.AbstractMemoryRepository;
import java.util.Objects;
import java.util.Optional;

public class MemoryMemberRepository extends AbstractMemoryRepository<Member> implements MemberRepository {

    @Override
    public void delete(Member member) {
        memory.remove(member.getId());
    }

    @Override
    public boolean existsById(Long id) {
        return memory.containsKey(id);
    }

    @Override
    public long count() {
        return memory.size();
    }

    @Override
    public Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType) {
        return memory.values().stream()
            .filter(member -> Objects.equals(member.getSocialId(), socialId))
            .filter(member -> Objects.equals(member.getSocialType(), socialType))
            .findAny();
    }
}
