package com.festago.ticket.repository;

import com.festago.ticket.domain.TicketAmount;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketAmountRepository extends JpaRepository<TicketAmount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ta from TicketAmount ta where ta.id = :ticketId")
    Optional<TicketAmount> findByTicketIdForUpdate(@Param("ticketId") Long ticketId);
}
