package com.festago.ticket.application.command.stage;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.ticket.domain.StageTicket;
import com.festago.ticket.domain.TicketExclusive;
import com.festago.ticket.dto.command.StageTicketCreateCommand;
import com.festago.ticket.dto.event.TicketCreatedEvent;
import com.festago.ticket.repository.StageTicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StageTicketCreateService {

    private final StageTicketRepository stageTicketRepository;
    private final StageRepository stageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public Long createStageTicket(StageTicketCreateCommand command) {
        Long schoolId = command.schoolId();
        Long stageId = command.stageId();
        TicketExclusive ticketType = command.ticketExclusive();
        StageTicket stageTicket = stageTicketRepository.findByStageIdAndTicketExclusiveWithFetch(stageId, ticketType)
            .orElseGet(() -> {
                Stage stage = findStage(stageId);
                return stageTicketRepository.save(new StageTicket(schoolId, ticketType, stage));
            });
        LocalDateTime entryTime = command.entryTime();
        int amount = command.amount();
        stageTicket.addTicketEntryTime(schoolId, LocalDateTime.now(clock), entryTime, amount);
        eventPublisher.publishEvent(new TicketCreatedEvent(stageTicket));
        return stageTicket.getId();
    }

    private Stage findStage(Long stageId) {
        return stageRepository.findByIdWithFetchFestival(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
    }
}
