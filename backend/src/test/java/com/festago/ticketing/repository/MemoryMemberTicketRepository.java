package com.festago.ticketing.repository;

import com.festago.member.domain.Member;
import com.festago.stage.domain.Stage;
import com.festago.ticketing.domain.MemberTicket;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;

public class MemoryMemberTicketRepository implements MemberTicketRepository {

    private final Map<Long, MemberTicket> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @SneakyThrows
    @Override
    public MemberTicket save(MemberTicket memberTicket) {
        Field idField = memberTicket.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(memberTicket, autoIncrement.incrementAndGet());
        memory.put(memberTicket.getId(), memberTicket);
        return memberTicket;
    }

    @Override
    public Optional<MemberTicket> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public List<MemberTicket> findAllByOwnerId(Long memberId, Pageable pageable) {
        return memory.values().stream()
            .filter(memberTicket -> memberTicket.isOwner(memberId))
            .toList();
    }

    @Override
    public boolean existsByOwnerAndStage(Member owner, Stage stage) {
        return memory.values().stream()
            .anyMatch(
                memberTicket -> memberTicket.getOwner().getId().equals(owner.getId()) && memberTicket.getStage().getId()
                    .equals(stage.getId()));
    }

    @Override
    public long count() {
        return memory.size();
    }

    public void clear() {
        memory.clear();
    }
}
