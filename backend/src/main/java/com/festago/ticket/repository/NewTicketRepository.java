package com.festago.ticket.repository;

import com.festago.ticket.domain.NewTicket;
import java.util.Optional;
import org.springframework.data.repository.Repository;

// TODO NewTicket -> Ticket 이름 변경할 것
public interface NewTicketRepository extends Repository<NewTicket, Long> {

    NewTicket save(NewTicket ticket);

    Optional<NewTicket> findById(Long id);
}
