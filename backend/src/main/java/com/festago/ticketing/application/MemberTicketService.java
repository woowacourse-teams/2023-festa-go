package com.festago.ticketing.application;

import static java.util.Comparator.comparing;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.dto.MemberTicketResponse;
import com.festago.ticketing.dto.MemberTicketsResponse;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberTicketService {

    private final MemberTicketRepository memberTicketRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public MemberTicketResponse findById(Long memberId, Long memberTicketId) {
        MemberTicket memberTicket = memberTicketRepository.getOrThrow(memberTicketId);
        validateOwner(memberTicket, memberId);
        return MemberTicketResponse.from(memberTicket);
    }

    private void validateOwner(MemberTicket memberTicket, Long memberId) {
        if (!memberTicket.isOwner(memberId)) {
            throw new BadRequestException(ErrorCode.NOT_MEMBER_TICKET_OWNER);
        }
    }

    @Transactional(readOnly = true)
    public MemberTicketsResponse findAll(Long memberId, Pageable pageable) {
        List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(memberId, pageable);
        return MemberTicketsResponse.from(memberTickets);
    }

    @Transactional(readOnly = true)
    public MemberTicketsResponse findCurrent(Long memberId, Pageable pageable) {
        List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(memberId, pageable);
        return MemberTicketsResponse.from(filterCurrentMemberTickets(memberTickets));
    }

    private List<MemberTicket> filterCurrentMemberTickets(List<MemberTicket> memberTickets) {
        LocalDateTime currentTime = LocalDateTime.now(clock);
        return memberTickets.stream()
            .filter(memberTicket -> memberTicket.isBeforeEntry(currentTime) || memberTicket.canEntry(currentTime))
            .sorted(comparing((MemberTicket memberTicket) -> memberTicket.isBeforeEntry(currentTime))
                .thenComparing(memberTicket -> calculateTimeGap(memberTicket, currentTime)))
            .toList();
    }

    private Duration calculateTimeGap(MemberTicket memberTicket, LocalDateTime time) {
        return Duration.between(memberTicket.getEntryTime(), time).abs();
    }
}
