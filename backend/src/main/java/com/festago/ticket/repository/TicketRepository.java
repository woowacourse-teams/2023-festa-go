package com.festago.ticket.repository;

import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
            SELECT t from Ticket t
            INNER JOIN FETCH t.ticketAmount
            WHERE t.stage.id = :stageId
        """)
    List<Ticket> findAllByStageIdWithFetch(@Param("stageId") Long stageId);

    Optional<Ticket> findByTicketTypeAndStage(TicketType ticketType, Stage stage);

    @Query("""
        SELECT t FROM Ticket t
        JOIN FETCH t.stage s
        JOIN FETCH t.ticketEntryTimes et
        WHERE t.id = :ticketId
        """)
    Optional<Ticket> findByIdWithFetch(@Param("ticketId") Long ticketId);
}
