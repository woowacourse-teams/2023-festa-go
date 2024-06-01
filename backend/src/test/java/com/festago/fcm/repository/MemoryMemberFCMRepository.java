package com.festago.fcm.repository;

import com.festago.fcm.domain.MemberFCM;
import com.festago.support.AbstractMemoryRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MemoryMemberFCMRepository extends AbstractMemoryRepository<MemberFCM> implements MemberFCMRepository {

    @Override
    public List<MemberFCM> findAllByMemberId(Long memberId) {
        return memory.values().stream()
            .filter(it -> Objects.equals(it.getMemberId(), memberId))
            .toList();
    }

    @Override
    public boolean existsByMemberIdAndFcmToken(Long memberId, String fcmToken) {
        return memory.values().stream()
            .filter(it -> Objects.equals(it.getMemberId(), memberId))
            .anyMatch(it -> Objects.equals(it.getFcmToken(), fcmToken));
    }

    @Override
    public Optional<MemberFCM> findByMemberIdAndFcmToken(Long memberId, String fcmToken) {
        return memory.values().stream()
            .filter(it -> Objects.equals(it.getMemberId(), memberId))
            .filter(it -> Objects.equals(it.getFcmToken(), fcmToken))
            .findAny();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        memory.entrySet().removeIf(it -> Objects.equals(it.getValue().getMemberId(), memberId));
    }

    @Override
    public void deleteByIn(List<MemberFCM> memberFCMs) {
        for (MemberFCM memberFCM : memberFCMs) {
            memory.remove(memberFCM.getId());
        }
    }
}
