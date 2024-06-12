package com.festago.ticket.application.command;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.ticket.domain.NewTicket;
import com.festago.ticket.repository.NewTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketMaxReserveCountChangeService {

    private final NewTicketRepository ticketRepository;

    public void changeMaxReserveAmount(Long ticketId, int maxReserveAmount) {
        NewTicket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
        ticket.changeMaxReserveAmount(maxReserveAmount);
    }
}
