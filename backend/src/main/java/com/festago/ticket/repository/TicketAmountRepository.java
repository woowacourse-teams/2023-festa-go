package com.festago.ticket.repository;

import com.festago.ticket.domain.TicketAmount;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface TicketAmountRepository extends Repository<TicketAmount, Long>, TicketAmountRepositoryCustom {

    Optional<TicketAmount> findById(Long id);
}
