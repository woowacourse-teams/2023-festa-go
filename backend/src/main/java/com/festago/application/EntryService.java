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
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntryService {

    private final EntryCodeProvider entryCodeProvider;
    private final EntryCodeExtractor entryCodeExtractor;
    private final MemberTicketRepository memberTicketRepository;

    public EntryService(EntryCodeProvider entryCodeProvider, EntryCodeExtractor entryCodeExtractor,
                        MemberTicketRepository memberTicketRepository) {
        this.entryCodeProvider = entryCodeProvider;
        this.entryCodeExtractor = entryCodeExtractor;
        this.memberTicketRepository = memberTicketRepository;
    }

    public EntryCodeResponse createEntryCode(Long memberId, Long memberTicketId) {
        MemberTicket memberTicket = findMemberTicket(memberTicketId);
        if (!memberTicket.isOwner(memberId)) {
            throw new IllegalArgumentException(); // TODO
        }
        if (!memberTicket.canEntry(LocalDateTime.now())) {
            throw new IllegalArgumentException(); // TODO
        }
        EntryCode entryCode = EntryCode.create(entryCodeProvider, memberTicket);
        return EntryCodeResponse.of(entryCode);
    }

    public TicketValidationResponse validate(TicketValidationRequest request) {
        EntryCodePayload entryCodePayload = entryCodeExtractor.extract(request.code());
        MemberTicket memberTicket = findMemberTicket(entryCodePayload.getMemberTicketId());
        memberTicket.changeState(entryCodePayload.getEntryState());
        return TicketValidationResponse.from(memberTicket);
    }

    private MemberTicket findMemberTicket(Long memberTicketId) {
        return memberTicketRepository.findById(memberTicketId)
            .orElseThrow(NoSuchElementException::new);
    }
}
