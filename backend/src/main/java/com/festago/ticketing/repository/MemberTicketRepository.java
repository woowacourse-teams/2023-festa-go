package com.festago.ticketing.repository;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.stage.domain.Stage;
import com.festago.ticketing.domain.MemberTicket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface MemberTicketRepository extends Repository<MemberTicket, Long> {

    default MemberTicket getOrThrow(Long memberTicketId) {
        return findById(memberTicketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_TICKET_NOT_FOUND));
    }

    MemberTicket save(MemberTicket memberTicket);

    Optional<MemberTicket> findById(Long id);

    List<MemberTicket> findAllByOwnerId(Long memberId, Pageable pageable);

    boolean existsByOwnerAndStage(Member owner, Stage stage);

    long count();
}
