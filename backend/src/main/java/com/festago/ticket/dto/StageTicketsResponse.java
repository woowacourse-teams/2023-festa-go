package com.festago.ticket.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.ticket.domain.Ticket;
import java.util.List;

public record StageTicketsResponse(
    List<StageTicketResponse> tickets) {

    public static StageTicketsResponse from(List<Ticket> tickets) {
        return tickets.stream()
            .map(StageTicketResponse::from)
            .collect(collectingAndThen(toList(), StageTicketsResponse::new));
    }
}
