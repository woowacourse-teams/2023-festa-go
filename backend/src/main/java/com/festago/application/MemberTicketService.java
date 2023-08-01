package com.festago.application;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.dto.CurrentMemberTicketsResponse;
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
        return memberTickets.stream()
            .sorted(comparing(memberTicket -> memberTicket.getStage().getStartTime()))
            .collect(collectingAndThen(toList(), MemberTicketsResponse::from));
    }

    @Transactional(readOnly = true)
    public CurrentMemberTicketsResponse findCurrent(Long memberId) {
        List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(memberId);
        LocalDateTime currentTime = LocalDateTime.now();
        List<MemberTicket> currentMemberTickets = filterCurrentMemberTickets(memberTickets, currentTime);
        List<MemberTicket> activateMemberTickets = filterActivateMemberTickets(currentMemberTickets, currentTime);
        List<MemberTicket> pendingMemberTickets = filterPendingMemberTickets(currentMemberTickets, currentTime);
        return CurrentMemberTicketsResponse.from(mergeMemberTickets(activateMemberTickets, pendingMemberTickets));
    }

    private List<MemberTicket> filterCurrentMemberTickets(List<MemberTicket> memberTickets,
                                                          LocalDateTime currentTime) {
        return memberTickets.stream()
            .filter(memberTicket -> memberTicket.isPending(currentTime) || memberTicket.canEntry(currentTime))
            .sorted(comparing(this::getTimeGapFromNow))
            .toList();
    }

    private List<MemberTicket> filterActivateMemberTickets(List<MemberTicket> memberTickets,
                                                           LocalDateTime currentTime) {
        return memberTickets.stream()
            .filter(memberTicket -> memberTicket.canEntry(currentTime))
            .toList();
    }

    private List<MemberTicket> filterPendingMemberTickets(List<MemberTicket> memberTickets, LocalDateTime currentTime) {
        return memberTickets.stream()
            .filter(memberTicket -> memberTicket.isPending(currentTime))
            .toList();
    }

    private long getTimeGapFromNow(MemberTicket memberTicket) {
        LocalDateTime now = LocalDateTime.now();
        return Math.abs(Duration.between(memberTicket.getEntryTime(), now).toMillis());
    }

    private List<MemberTicket> mergeMemberTickets(List<MemberTicket> memberTickets1,
                                                  List<MemberTicket> memberTickets2) {
        return concat(memberTickets1.stream(), memberTickets2.stream())
            .toList();
    }
}
