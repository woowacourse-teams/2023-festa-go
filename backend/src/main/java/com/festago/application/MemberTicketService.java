package com.festago.application;

import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.dto.MemberTicketResponse;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberTicketService {

    private final MemberTicketRepository memberTicketRepository;

    public MemberTicketService(MemberTicketRepository memberTicketRepository) {
        this.memberTicketRepository = memberTicketRepository;
    }

    @Transactional(readOnly = true)
    public MemberTicketResponse findById(Long memberId, Long memberTicketId) {
        MemberTicket memberTicket = memberTicketRepository.findById(memberTicketId)
                .orElseThrow(NoSuchElementException::new);
        if (!memberTicket.isOwner(memberId)) {
            throw new IllegalArgumentException(); // TODO
        }
        return MemberTicketResponse.from(memberTicket);
    }
}
