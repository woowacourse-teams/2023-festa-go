package com.festago.ticket.repository;

import com.festago.ticket.domain.TicketAmount;
import java.util.Optional;

@Deprecated(forRemoval = true)
public interface TicketAmountRepositoryCustom {

    Optional<TicketAmount> findByTicketIdForUpdate(Long ticketId);
}
