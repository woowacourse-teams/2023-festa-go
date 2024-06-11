package com.festago.ticket.application.command.stage;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.ticket.domain.StageTicket;
import com.festago.ticket.dto.command.StageTicketDeleteCommand;
import com.festago.ticket.dto.event.TicketDeletedEvent;
import com.festago.ticket.repository.StageTicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StageTicketDeleteService {

    private final StageTicketRepository stageTicketRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public void deleteStageTicket(StageTicketDeleteCommand command) {
        Long ticketId = command.stageTicketId();
        StageTicket stageTicket = stageTicketRepository.findByIdWithFetch(ticketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
        Long schoolId = command.schoolId();
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime entryTime = command.entryTime();
        boolean isDeleted = stageTicket.deleteTicketEntryTime(schoolId, now, entryTime);
        if (stageTicket.isEmptyAmount()) {
            stageTicketRepository.deleteById(ticketId);
        }
        if (isDeleted) {
            eventPublisher.publishEvent(new TicketDeletedEvent(stageTicket));
        }
    }
}
