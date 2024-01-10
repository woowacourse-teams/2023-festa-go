package com.festago.ticket.repository;

import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface TicketRepository extends Repository<Ticket, Long>, TicketRepositoryCustom {

    Optional<Ticket> findByTicketTypeAndStage(TicketType ticketType, Stage stage);

    Ticket save(Ticket ticket);

    long count();
}
