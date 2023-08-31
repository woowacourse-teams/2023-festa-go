package com.festago.application;

import com.festago.domain.EntryCode;
import com.festago.domain.EntryCodeExtractor;
import com.festago.domain.EntryCodePayload;
import com.festago.domain.EntryCodeProvider;
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

    private final EntryCodeProvider entryCodeProvider;
    private final EntryCodeExtractor entryCodeExtractor;
    private final MemberTicketRepository memberTicketRepository;
    private final Clock clock;

    public EntryService(EntryCodeProvider entryCodeProvider, EntryCodeExtractor entryCodeExtractor,
                        MemberTicketRepository memberTicketRepository, Clock clock) {
        this.entryCodeProvider = entryCodeProvider;
        this.entryCodeExtractor = entryCodeExtractor;
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
        EntryCode entryCode = EntryCode.create(entryCodeProvider, memberTicket);
        return EntryCodeResponse.of(entryCode);
    }

    private MemberTicket findMemberTicket(Long memberTicketId) {
        return memberTicketRepository.findById(memberTicketId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_TICKET_NOT_FOUND));
    }

    public TicketValidationResponse validate(TicketValidationRequest request) {
        EntryCodePayload entryCodePayload = entryCodeExtractor.extract(request.code());
        MemberTicket memberTicket = findMemberTicket(entryCodePayload.getMemberTicketId());
        memberTicket.changeState(entryCodePayload.getEntryState());
        return TicketValidationResponse.from(memberTicket);
    }
}
