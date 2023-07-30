package com.festago.domain;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByStageId(Long stageId);

    Optional<Ticket> findByTicketTypeAndStage(TicketType ticketType, Stage stage);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Ticket> findByTicketTypeAndStageId(TicketType ticketType, Long stageId);
}
