package com.festago.ticketing.repository;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticketing.domain.TicketSequence;
import java.util.Optional;

/**
 * TicketSequence를 저장, 삭제, 조회하는 Repository
 */
public interface TicketSequenceRepository {

    /**
     * Ticket에 대한 정보를 가진 TicketSequence를 만들어 저장한다. <br/> 기존 TicketSequence가 저장되어 있으면 덮어쓴다. <br/>
     *
     * @param ticket 저장할 TicketSequence에 대한 Ticket 엔티티
     * @return TicketSequence
     */
    TicketSequence put(NewTicket ticket);

    /**
     * Ticket의 식별자에 대한 TicketSequence를 반환한다. <br/>
     *
     * @param ticketId Ticket의 식별자
     * @return TicketSequence
     */
    Optional<TicketSequence> findByTicketId(Long ticketId);

    /**
     * Ticket에 대한 TicketSequence를 삭제한다. <br/>
     *
     * @param ticket 삭제할 TicketSequence에 대한 Ticket 엔티티
     */
    void delete(NewTicket ticket);
}
