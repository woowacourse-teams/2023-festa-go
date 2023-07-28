package com.festago.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByStageId(Long stageId);

    Optional<Ticket> findByTicketTypeAndStage(TicketType ticketType, Stage stage);
}
