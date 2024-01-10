package com.festago.ticket.repository;

import com.festago.ticket.domain.Ticket;
import java.util.List;
import java.util.Optional;

public interface TicketRepositoryCustom {

    List<Ticket> findAllByStageIdWithFetch(Long stageId);

    Optional<Ticket> findByIdWithFetch(Long ticketId);
}
