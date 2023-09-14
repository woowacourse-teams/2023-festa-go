package com.festago.application;

import com.festago.domain.EntryCode;
import com.festago.domain.EntryCodePayload;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.dto.EntryCodeResponse;
import com.festago.dto.TicketValidationRequest;
import com.festago.dto.TicketValidationResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntryService {

    private final EntryCodeManager entryCodeManager;
    private final MemberTicketRepository memberTicketRepository;
    private final Clock clock;

    public EntryService(EntryCodeManager entryCodeManager, MemberTicketRepository memberTicketRepository, Clock clock) {
        this.entryCodeManager = entryCodeManager;
        this.memberTicketRepository = memberTicketRepository;
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
        return TicketValidationResponse.from(memberTicket);
    }
}
