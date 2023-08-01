package com.festago.application;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.dto.CurrentMemberTicketsResponse;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
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
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_TICKET_NOT_FOUND));
        if (!memberTicket.isOwner(memberId)) {
            throw new BadRequestException(ErrorCode.NOT_MEMBER_TICKET_OWNER);
        }
        return MemberTicketResponse.from(memberTicket);
    }

    @Transactional(readOnly = true)
    public MemberTicketsResponse findAll(Long memberId) {
        List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(memberId);
        return MemberTicketsResponse.from(memberTickets);
    }

    @Transactional(readOnly = true)
    public CurrentMemberTicketsResponse findCurrentMemberTickets(Long memberId) {
        List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(memberId);
        return memberTickets.stream()
            .filter(memberTicket -> memberTicket.isCurrent(LocalDateTime.now()))
            .sorted(Comparator.comparing(MemberTicket::getEntryTime))
            .collect(collectingAndThen(toList(), CurrentMemberTicketsResponse::from));
    }
}
