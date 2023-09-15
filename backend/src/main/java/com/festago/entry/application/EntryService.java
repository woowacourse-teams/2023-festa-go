package com.festago.entry.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.entry.domain.EntryCode;
import com.festago.entry.domain.EntryCodePayload;
import com.festago.entry.dto.EntryCodeResponse;
import com.festago.entry.dto.EntryProcessEvent;
import com.festago.entry.dto.TicketValidationRequest;
import com.festago.entry.dto.TicketValidationResponse;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntryService {

    private final EntryCodeManager entryCodeManager;
    private final MemberTicketRepository memberTicketRepository;
    private final ApplicationEventPublisher publisher;
    private final Clock clock;

    public EntryService(EntryCodeManager entryCodeManager, MemberTicketRepository memberTicketRepository,
                        ApplicationEventPublisher publisher, Clock clock) {
        this.entryCodeManager = entryCodeManager;
        this.memberTicketRepository = memberTicketRepository;
        this.publisher = publisher;
        this.clock = clock;
    }

    public EntryCodeResponse createEntryCode(Long memberId, Long memberTicketId) {
        MemberTicket memberTicket = findMemberTicket(memberTicketId);
        if (!memberTicket.isOwner(memberId)) {
            throw new BadRequestException(ErrorCode.NOT_MEMBER_TICKET_OWNER);
        }
        if (!memberTicket.canEntry(LocalDateTime.now(clock))) {
            throw new BadRequestException(ErrorCode.NOT_ENTRY_TIME);
        }
        EntryCode entryCode = entryCodeManager.provide(EntryCodePayload.from(memberTicket), clock.millis());
        return EntryCodeResponse.of(entryCode);
    }

    private MemberTicket findMemberTicket(Long memberTicketId) {
        return memberTicketRepository.findById(memberTicketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_TICKET_NOT_FOUND));
    }

    public TicketValidationResponse validate(TicketValidationRequest request) {
        EntryCodePayload entryCodePayload = entryCodeManager.extract(request.code());
        MemberTicket memberTicket = findMemberTicket(entryCodePayload.getMemberTicketId());
        memberTicket.changeState(entryCodePayload.getEntryState());
        publisher.publishEvent(new EntryProcessEvent(memberTicket.getOwner().getId()));
        return TicketValidationResponse.from(memberTicket);
    }
}
