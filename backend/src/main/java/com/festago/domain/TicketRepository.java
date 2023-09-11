package com.festago.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByStageId(Long stageId);

    Optional<Ticket> findByTicketTypeAndStage(TicketType ticketType, Stage stage);

    @Query("""
        SELECT t FROM Ticket t
        JOIN FETCH t.stage s
        JOIN FETCH t.ticketEntryTimes et
        WHERE t.id = :ticketId
        """)
    Optional<Ticket> findByIdWithFetch(@Param("ticketId") Long ticketId);
}
