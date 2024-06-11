package com.festago.ticketing.application;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticket.dto.event.TicketCreatedEvent;
import com.festago.ticket.dto.event.TicketDeletedEvent;
import com.festago.ticketing.application.command.TicketSequenceUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TicketSequenceEventListener {

    private final TicketSequenceUpdateService ticketSequenceUpdateService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void ticketCreatedEventHandler(TicketCreatedEvent event) {
        NewTicket ticket = event.ticket();
        ticketSequenceUpdateService.putOrDeleteTicketSequence(ticket);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void ticketDeletedEventHandler(TicketDeletedEvent event) {
        NewTicket ticket = event.ticket();
        ticketSequenceUpdateService.putOrDeleteTicketSequence(ticket);
    }
}
