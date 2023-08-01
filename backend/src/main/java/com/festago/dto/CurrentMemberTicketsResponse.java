package com.festago.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.domain.MemberTicket;
import java.util.List;

public record CurrentMemberTicketsResponse(List<CurrentMemberTicketResponse> memberTickets) {

    public static CurrentMemberTicketsResponse from(List<MemberTicket> memberTickets) {
        return memberTickets.stream()
            .map(CurrentMemberTicketResponse::from)
            .collect(collectingAndThen(toList(), CurrentMemberTicketsResponse::new));
    }
}
