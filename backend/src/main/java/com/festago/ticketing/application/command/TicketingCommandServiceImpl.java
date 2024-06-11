package com.festago.ticketing.application.command;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.ticket.domain.NewTicket;
import com.festago.ticket.domain.NewTicketType;
import com.festago.ticket.repository.NewTicketDao;
import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.domain.ReserveTicket;
import com.festago.ticketing.domain.TicketingSequenceGenerator;
import com.festago.ticketing.domain.validator.TicketingValidator;
import com.festago.ticketing.dto.TicketingResult;
import com.festago.ticketing.dto.command.TicketingCommand;
import com.festago.ticketing.repository.ReserveTicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketingCommandServiceImpl implements TicketingCommandService {

    private final NewTicketDao ticketDao;
    private final ReserveTicketRepository reserveTicketRepository;
    private final TicketingSequenceGenerator sequenceGenerator;
    private final List<TicketingValidator> validators;
    private final Clock clock;

    @Override
    public TicketingResult reserveTicket(TicketingCommand command) {
        Long ticketId = command.ticketId();
        NewTicketType ticketType = command.ticketType();
        NewTicket ticket = ticketDao.findByIdWithTicketTypeAndFetch(ticketId, ticketType);
        Booker booker = command.booker();
        ticket.validateReserve(booker, LocalDateTime.now(clock));
        validators.forEach(validator -> validator.validate(ticket, booker));
        validate(ticket, booker);
        int sequence = sequenceGenerator.generate(ticketId);
        ReserveTicket reserveTicket = ticket.reserve(booker, sequence);
        reserveTicketRepository.save(ticket.reserve(booker, sequence));
        return new TicketingResult(reserveTicket.getTicketId());
    }

    private void validate(NewTicket ticket, Booker booker) {
        long reserveCount = reserveTicketRepository.countByMemberIdAndTicketId(booker.getMemberId(), ticket.getId());
        if (reserveCount >= ticket.getMaxReserveAmount()) {
            throw new BadRequestException(ErrorCode.RESERVE_TICKET_OVER_AMOUNT);
        }
    }
}
