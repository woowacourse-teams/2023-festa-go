package com.festago.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberTicketRepository extends JpaRepository<MemberTicket, Long> {

    List<MemberTicket> findAllByOwnerId(Long memberId);

    @Query("""
        SELECT COUNT(mt)
        FROM MemberTicket mt
        JOIN mt.ticket t
        WHERE t.ticketType = :ticketType AND t.stage.id = :stageId
        """)
    Integer countByTicketTypeAndStageId(@Param("ticketType") TicketType ticketType, @Param("stageId") Long stageId);
}
