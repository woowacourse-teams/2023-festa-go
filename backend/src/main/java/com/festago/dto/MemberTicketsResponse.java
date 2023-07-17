package com.festago.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.domain.MemberTicket;
import java.util.List;

public record MemberTicketsResponse(List<MemberTicketResponse> tickets) {

    public static MemberTicketsResponse from(List<MemberTicket> memberTickets) {
        return memberTickets.stream()
                .map(MemberTicketResponse::from)
                .collect(collectingAndThen(toList(), MemberTicketsResponse::new));
    }
}
