package com.festago.ticket.repository;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.ticket.domain.NewTicket;
import com.festago.ticket.domain.NewTicketType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// TODO NewTicket -> Ticket 이름 변경할 것
@Repository
@RequiredArgsConstructor
public class NewTicketDao {

    private final StageTicketRepository stageTicketRepository;

    public NewTicket findByIdWithTicketTypeAndFetch(Long id, NewTicketType ticketType) {
        Optional<? extends NewTicket> ticket = switch (ticketType) {
            case STAGE -> stageTicketRepository.findByIdWithFetch(id);
        };
        return ticket.orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
    }
}
