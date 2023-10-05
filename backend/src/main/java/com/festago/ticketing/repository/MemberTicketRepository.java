package com.festago.ticketing.repository;

import com.festago.member.domain.Member;
import com.festago.stage.domain.Stage;
import com.festago.ticketing.domain.MemberTicket;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberTicketRepository extends JpaRepository<MemberTicket, Long> {

    List<MemberTicket> findAllByOwnerId(Long memberId, Pageable pageable);

    boolean existsByOwnerAndStage(Member owner, Stage stage);

    @Query("""
        SELECT m.owner.id
        FROM MemberTicket m
        WHERE m.stage.id = :stageId
        AND m.entryTime = :entryTime
        """)
    List<Long> findAllOwnerIdByStageIdAndEntryTime(@Param("stageId") Long stageId,
                                                   @Param("entryTime") LocalDateTime entryTime);
}
