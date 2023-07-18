package com.festago.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTicketRepository extends JpaRepository<MemberTicket, Long> {

    List<MemberTicket> findAllByOwnerId(Long memberId);
}
