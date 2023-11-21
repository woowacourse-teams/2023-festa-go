package com.festago.ticket.repository;

import com.festago.ticket.domain.TicketAmount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketAmountRepository extends JpaRepository<TicketAmount, Long>, TicketAmountRepositoryCustom {
}
