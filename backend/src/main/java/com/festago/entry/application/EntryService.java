package com.festago.entry.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.entry.domain.EntryCode;
import com.festago.entry.domain.EntryCodePayload;
import com.festago.entry.dto.EntryCodeResponse;
import com.festago.entry.dto.TicketValidationRequest;
import com.festago.entry.dto.TicketValidationResponse;
import com.festago.entry.dto.event.EntryProcessEvent;
import com.festago.ticketing.domain.MemberTicket;
import com.festago.ticketing.repository.MemberTicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EntryService {

    private final EntryCodeManager entryCodeManager;
    private final MemberTicketRepository memberTicketRepository;
    private final ApplicationEventPublisher publisher;
    private final Clock clock;

    public EntryCodeResponse createEntryCode(Long memberId, Long memberTicketId) {
        MemberTicket memberTicket = memberTicketRepository.getOrThrow(memberTicketId);
        if (!memberTicket.isOwner(memberId)) {
            throw new BadRequestException(ErrorCode.NOT_MEMBER_TICKET_OWNER);
        }
        if (!memberTicket.canEntry(LocalDateTime.now(clock))) {
            throw new BadRequestException(ErrorCode.NOT_ENTRY_TIME);
        }
        EntryCode entryCode = entryCodeManager.provide(EntryCodePayload.from(memberTicket), clock.millis());
        return EntryCodeResponse.of(entryCode);
    }

    public TicketValidationResponse validate(TicketValidationRequest request) {
        EntryCodePayload entryCodePayload = entryCodeManager.extract(request.code());
        Long memberTicketId = entryCodePayload.getMemberTicketId();
        MemberTicket memberTicket = memberTicketRepository.getOrThrow(memberTicketId);
        memberTicket.changeState(entryCodePayload.getEntryState());
        publisher.publishEvent(new EntryProcessEvent(memberTicket.getOwner().getId()));
        return TicketValidationResponse.from(memberTicket);
    }
}
