package com.festago.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberTicketRepository extends JpaRepository<MemberTicket, Long> {

    List<MemberTicket> findAllByOwnerId(Long memberId, Pageable pageable);

    //TODO: entryTime이 아니라 createAt 필드로 정렬해서 가져오도록 변경
    @Query("""
            SELECT mt FROM MemberTicket mt  
            JOIN FETCH mt.stage s  
            JOIN FETCH s.festival  
            ORDER BY mt.entryTime DESC 
            LIMIT 1
        """)
    Optional<MemberTicket> findRecentlyReservedTicket(Long memberId);
}
