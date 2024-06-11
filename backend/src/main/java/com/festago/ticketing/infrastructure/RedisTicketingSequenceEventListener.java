package com.festago.ticketing.infrastructure;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticket.dto.event.TicketCreatedEvent;
import com.festago.ticket.dto.event.TicketDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RedisTicketingSequenceEventListener {

    private final RedisTicketingSequenceGenerator redisTicketingSequenceGenerator;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void ticketCreatedEventHandler(TicketCreatedEvent event) {
        NewTicket ticket = event.ticket();
        redisTicketingSequenceGenerator.setUp(ticket.getId(), ticket.getTicketingEndTime());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void ticketDeletedEventHandler(TicketDeletedEvent event) {
        NewTicket ticket = event.ticket();
        redisTicketingSequenceGenerator.delete(ticket.getId());
    }
}
