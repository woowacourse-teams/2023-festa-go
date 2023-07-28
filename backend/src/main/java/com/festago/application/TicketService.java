package com.festago.application;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketResponse;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.TicketType;
import com.festago.dto.StageTicketResponse;
import com.festago.dto.StageTicketsResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StageRepository stageRepository;
    private final MemberTicketRepository memberTicketRepository;

    public TicketService(TicketRepository ticketRepository, StageRepository stageRepository, MemberTicketRepository memberTicketRepository) {
        this.ticketRepository = ticketRepository;
        this.stageRepository = stageRepository;
        this.memberTicketRepository = memberTicketRepository;
    }

    public TicketResponse create(TicketCreateRequest request) {
        Stage stage = findStageById(request.stageId());
        Ticket newTicket = ticketRepository.save(new Ticket(stage,
            request.ticketType(),
            request.totalAmount(),
            request.entryTime()));

        return TicketResponse.from(newTicket);
    }

    private Stage findStageById(Long stageId) {
        return stageRepository.findById(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public StageTicketsResponse findStageTickets(Long stageId) {
        Map<TicketType, Integer> ticketTypeToTotalAmount = getTicketTypeToTotalAmount(stageId);
        return getStageTicketsResponse(ticketTypeToTotalAmount, stageId);
    }

    private Map<TicketType, Integer> getTicketTypeToTotalAmount(Long stageId) {
        List<Ticket> tickets = ticketRepository.findAllByStageId(stageId);
        return tickets.stream()
            .collect(groupingBy(Ticket::getTicketType, summingInt(Ticket::getTotalAmount)));
    }

    private StageTicketsResponse getStageTicketsResponse(Map<TicketType, Integer> ticketTypeToTotalAmount,
                                                         Long stageId) {
        List<StageTicketResponse> stageTicketResponses = new ArrayList<>();
        for (Entry<TicketType, Integer> entry : ticketTypeToTotalAmount.entrySet()) {
            Integer reserveCount = memberTicketRepository.countByTicketTypeAndStageId(entry.getKey(), stageId);
            stageTicketResponses.add(
                new StageTicketResponse(entry.getKey(), entry.getValue(), entry.getValue() - reserveCount));
        }

        return new StageTicketsResponse(stageTicketResponses);
    }
}
