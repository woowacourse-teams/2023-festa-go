package com.festago.ticket.repository;

import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Deprecated(forRemoval = true)
public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    Optional<Ticket> findByTicketTypeAndStage(TicketType ticketType, Stage stage);
}
