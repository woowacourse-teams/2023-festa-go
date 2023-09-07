package com.festago.application;

import com.festago.domain.Member;
import com.festago.domain.Ticket;
import java.util.Optional;

public interface TicketAmountService {

    Optional<Integer> getSequence(Ticket ticket, Member member);
}
