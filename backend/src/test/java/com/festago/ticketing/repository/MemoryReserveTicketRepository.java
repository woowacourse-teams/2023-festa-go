package com.festago.ticketing.repository;

import com.festago.support.AbstractMemoryRepository;
import com.festago.ticketing.domain.ReserveTicket;
import java.util.Objects;

public class MemoryReserveTicketRepository extends AbstractMemoryRepository<ReserveTicket> implements
    ReserveTicketRepository {

    @Override
    public long countByMemberIdAndTicketId(Long memberId, Long ticketId) {
        return memory.values().stream()
            .filter(it -> Objects.equals(it.getMemberId(), memberId))
            .filter(it -> Objects.equals(it.getTicketId(), ticketId))
            .count();
    }
}
