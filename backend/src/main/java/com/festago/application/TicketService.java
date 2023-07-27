package com.festago.application;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Ticket;
import com.festago.domain.TicketRepository;
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
    private final MemberTicketRepository memberTicketRepository;

    public TicketService(TicketRepository ticketRepository, MemberTicketRepository memberTicketRepository) {
        this.ticketRepository = ticketRepository;
        this.memberTicketRepository = memberTicketRepository;
    }

    public StageTicketsResponse findStageTickets(Long stageId) {
        List<Ticket> tickets = ticketRepository.findAllByStageId(stageId);
        Map<TicketType, Integer> collect = tickets.stream()
            .collect(groupingBy(Ticket::getTicketType, summingInt(Ticket::getTotalAmount)));

        List<StageTicketResponse> stageTicketResponses = new ArrayList<>();
        for (Entry<TicketType, Integer> entry : collect.entrySet()) {
            Integer reserveCount = memberTicketRepository.countByTicketTypeAndStageId(entry.getKey(), stageId);
            stageTicketResponses.add(
                new StageTicketResponse(entry.getKey(), entry.getValue(), entry.getValue() - reserveCount));
        }

        return new StageTicketsResponse(stageTicketResponses);
    }
}
