package com.festago.ticketing.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.ticketing.domain.MemberTicket;
import java.util.List;

@Deprecated(forRemoval = true)
public record MemberTicketsResponse(
    List<MemberTicketResponse> memberTickets) {

    public static MemberTicketsResponse from(List<MemberTicket> memberTickets) {
        return memberTickets.stream()
            .map(MemberTicketResponse::from)
            .collect(collectingAndThen(toList(), MemberTicketsResponse::new));
    }
}
