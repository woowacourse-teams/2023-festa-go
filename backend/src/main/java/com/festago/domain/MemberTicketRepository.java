package com.festago.domain;

import com.festago.stage.domain.Stage;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTicketRepository extends JpaRepository<MemberTicket, Long> {

    List<MemberTicket> findAllByOwnerId(Long memberId, Pageable pageable);

    boolean existsByOwnerAndStage(Member owner, Stage stage);
}
