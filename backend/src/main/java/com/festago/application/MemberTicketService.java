package com.festago.application;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberTicketService {

    private final MemberTicketRepository memberTicketRepository;
    private final MemberRepository memberRepository;

    public MemberTicketService(MemberTicketRepository memberTicketRepository,
                               MemberRepository memberRepository) {
        this.memberTicketRepository = memberTicketRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public MemberTicketResponse findById(Long memberId, Long memberTicketId) {
        validateMemberId(memberId);
        MemberTicket memberTicket = memberTicketRepository.findById(memberTicketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_TICKET_NOT_FOUND));
        if (!memberTicket.isOwner(memberId)) {
            throw new BadRequestException(ErrorCode.NOT_MEMBER_TICKET_OWNER);
        }
        return MemberTicketResponse.from(memberTicket);
    }

    @Transactional(readOnly = true)
    public MemberTicketsResponse findAll(Long memberId) {
        validateMemberId(memberId);
        List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(memberId);
        return memberTickets.stream()
            .sorted(comparing(memberTicket -> memberTicket.getStage().getStartTime()))
            .collect(collectingAndThen(toList(), MemberTicketsResponse::from));
    }

    @Transactional(readOnly = true)
    public MemberTicketsResponse findCurrent(Long memberId) {
        validateMemberId(memberId);
        List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(memberId);
        return MemberTicketsResponse.from(filterCurrentMemberTickets(memberTickets));
    }

    private List<MemberTicket> filterCurrentMemberTickets(List<MemberTicket> memberTickets) {
        LocalDateTime currentTime = LocalDateTime.now();
        return memberTickets.stream()
            .filter(memberTicket -> memberTicket.isPending(currentTime) || memberTicket.canEntry(currentTime))
            .sorted(comparing((MemberTicket memberTicket) -> memberTicket.isPending(currentTime))
                .thenComparing(memberTicket -> calculateTimeGap(memberTicket, currentTime)))
            .toList();
    }

    private Duration calculateTimeGap(MemberTicket memberTicket, LocalDateTime time) {
        return Duration.between(memberTicket.getEntryTime(), time).abs();
    }

    private void validateMemberId(Long memberId) {
        memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
