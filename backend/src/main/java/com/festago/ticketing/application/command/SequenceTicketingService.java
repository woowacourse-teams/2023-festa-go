package com.festago.ticketing.application.command;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.exception.TooManyRequestException;
import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.domain.TicketSequence;
import com.festago.ticketing.domain.TicketingRateLimiter;
import com.festago.ticketing.dto.TicketingResult;
import com.festago.ticketing.dto.command.TicketingCommand;
import com.festago.ticketing.repository.TicketSequenceRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@Transactional 명시적으로 Transactional 사용하지 않음
public class SequenceTicketingService {

    private final TicketingCommandService ticketingCommandService;
    private final TicketSequenceRepository ticketSequenceRepository;
    private final TicketingRateLimiter ticketingRateLimiter;

    public TicketingResult ticketing(TicketingCommand command) {
        Long ticketId = command.ticketId();
        TicketSequence ticketSequence = getTicketSequence(ticketId);
        validateFrequentTicketing(command.booker());
        int sequence = ticketSequence.reserve();
        try {
            return ticketingCommandService.ticketing(command, sequence);
        } catch (Exception e) {
            ticketSequence.cancel(sequence);
            throw e;
        }
    }

    private void validateFrequentTicketing(Booker booker) {
        if (ticketingRateLimiter.isFrequentTicketing(booker, 5, TimeUnit.SECONDS)) {
            throw new TooManyRequestException();
        }
    }

    private TicketSequence getTicketSequence(Long ticketId) {
        TicketSequence ticketSequence = ticketSequenceRepository.findByTicketId(ticketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
        if (ticketSequence.isSoldOut()) {
            throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
        }
        return ticketSequence;
    }
}
