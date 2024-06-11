package com.festago.ticket.repository;

import com.festago.support.AbstractMemoryRepository;
import com.festago.ticket.domain.StageTicket;
import com.festago.ticket.domain.TicketExclusive;
import java.util.Objects;
import java.util.Optional;

public class MemoryStageTicketRepository extends AbstractMemoryRepository<StageTicket> implements
    StageTicketRepository {

    @Override
    public boolean existsByStageId(Long stageId) {
        return memory.values().stream()
            .anyMatch(it -> Objects.equals(it.getStage().getId(), stageId));
    }

    @Override
    public Optional<StageTicket> findByIdWithFetch(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public Optional<StageTicket> findByStageIdAndTicketExclusiveWithFetch(Long stageId, TicketExclusive ticketType) {
        return memory.values().stream()
            .filter(it -> Objects.equals(it.getStage().getId(), stageId))
            .filter(it -> it.getTicketExclusive() == ticketType)
            .findAny();
    }
}
