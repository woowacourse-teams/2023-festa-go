package com.festago.fcm.repository;

import com.festago.fcm.domain.MemberFCM;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryMemberFCMRepository implements MemberFCMRepository {

    private final Map<Long, MemberFCM> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @SneakyThrows
    @Override
    public MemberFCM save(MemberFCM memberFCM) {
        Field idField = memberFCM.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(memberFCM, autoIncrement.incrementAndGet());
        memory.put(memberFCM.getId(), memberFCM);
        return memberFCM;
    }

    @Override
    public List<MemberFCM> findByMemberId(Long memberId) {
        return memory.values().stream()
            .filter(memberFCM -> memberFCM.getMemberId().equals(memberId))
            .toList();
    }

    @Override
    public Optional<MemberFCM> findByMemberIdAndFcmToken(Long memberId, String fcmToken) {
        return memory.values().stream()
            .filter(memberFCM -> memberFCM.getMemberId().equals(memberId) && memberFCM.getFcmToken().equals(fcmToken))
            .findAny();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        memory.entrySet()
            .removeIf(entry -> entry.getValue().getMemberId().equals(memberId));
    }

    public void clear() {
        memory.clear();
    }
}
