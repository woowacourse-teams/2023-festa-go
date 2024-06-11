package com.festago.ticket.repository;

import com.festago.ticket.domain.StageTicket;
import com.festago.ticket.domain.TicketExclusive;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface StageTicketRepository extends Repository<StageTicket, Long> {

    StageTicket save(StageTicket stageTicket);

    Optional<StageTicket> findById(Long id);

    boolean existsByStageId(Long stageId);

    void deleteById(Long id);

    @Query("""
            select st
            from StageTicket st
            join fetch st.stage
            join fetch st.ticketEntryTimes
            where st.id = :id
        """)
    Optional<StageTicket> findByIdWithFetch(@Param("id") Long id);

    @Query("""
            select st
            from StageTicket st
            join fetch st.stage
            join fetch st.ticketEntryTimes
            where st.stage.id = :stageId and st.ticketExclusive = :ticketExclusive
        """)
    Optional<StageTicket> findByStageIdAndTicketExclusiveWithFetch(
        @Param("stageId") Long stageId,
        @Param("ticketExclusive") TicketExclusive ticketExclusive
    );
}
