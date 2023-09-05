package com.festago.application;

import com.festago.domain.Ticket;
import java.util.Optional;

public interface TicketAmountService {

    Optional<Integer> getSequence(Ticket ticket);
}
