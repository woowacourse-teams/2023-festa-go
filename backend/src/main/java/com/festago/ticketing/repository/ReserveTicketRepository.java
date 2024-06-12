package com.festago.ticketing.repository;

import com.festago.ticketing.domain.ReserveTicket;
import org.springframework.data.repository.Repository;

public interface ReserveTicketRepository extends Repository<ReserveTicket, Long> {

    ReserveTicket save(ReserveTicket reserveTicket);

    long countByMemberIdAndTicketId(Long memberId, Long ticketId);
}
