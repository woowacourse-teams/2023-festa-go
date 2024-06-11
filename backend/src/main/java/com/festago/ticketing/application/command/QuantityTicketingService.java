package com.festago.ticketing.application.command;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.domain.TicketQuantity;
import com.festago.ticketing.domain.TicketingRateLimiter;
import com.festago.ticketing.dto.TicketingResult;
import com.festago.ticketing.dto.command.TicketingCommand;
import com.festago.ticketing.repository.TicketQuantityRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@Transactional 명시적으로 Transactional 사용하지 않음
public class QuantityTicketingService {

    private final TicketQuantityRepository ticketQuantityRepository;
    private final TicketingCommandService ticketingCommandService;
    private final TicketingRateLimiter ticketingRateLimiter;

    public TicketingResult ticketing(TicketingCommand command) {
        TicketQuantity ticketQuantity = getTicketQuantity(command.ticketId());
        validateFrequentTicketing(command.booker());
        try {
            ticketQuantity.decreaseQuantity();
            return ticketingCommandService.reserveTicket(command);
        } catch (Exception e) {
            ticketQuantity.increaseQuantity();
            throw e;
        }
    }

    private TicketQuantity getTicketQuantity(Long ticketId) {
        TicketQuantity ticketQuantity = ticketQuantityRepository.findByTicketId(ticketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.TICKET_NOT_FOUND));
        if (ticketQuantity.isSoldOut()) {
            throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
        }
        return ticketQuantity;
    }

    private void validateFrequentTicketing(Booker booker) {
        if (ticketingRateLimiter.isFrequentTicketing(booker, 5, TimeUnit.SECONDS)) {
            throw new BadRequestException(ErrorCode.TOO_FREQUENT_REQUESTS);
        }
    }
}
