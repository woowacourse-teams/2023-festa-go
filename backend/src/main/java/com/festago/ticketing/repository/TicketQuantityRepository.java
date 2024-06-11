package com.festago.ticketing.repository;

import com.festago.ticket.domain.NewTicket;
import com.festago.ticketing.domain.TicketQuantity;
import java.util.Optional;

/**
 * TicketQuantity를 저장, 삭제, 조회하는 Repository
 */
public interface TicketQuantityRepository {

    /**
     * Ticket에 대한 정보를 가진 TicketQuantity를 만들어 저장한다. <br/>
     * 기존 TicketQuantity가 저장되어 있으면 덮어쓴다. <br/>
     *
     * @param ticket 저장할 TicketQuantity에 대한 Ticket 엔티티
     * @return TicketQuantity
     */
    TicketQuantity put(NewTicket ticket);

    /**
     * Ticket의 식별자에 대한 TicketQuantity를 반환한다. <br/>
     *
     * @param ticketId Ticket의 식별자
     * @return TicketQuantity
     */
    Optional<TicketQuantity> findByTicketId(Long ticketId);

    /**
     * Ticket에 대한 TicketQuantity를 삭제한다. <br/>
     *
     * @param ticket 삭제할 TicketQuantity에 대한 Ticket 엔티티
     */
    void delete(NewTicket ticket);
}
