package com.festago.application;

import com.festago.domain.EntryCode;
import com.festago.domain.EntryCodeProvider;
import com.festago.domain.MemberTicket;
import com.festago.domain.MemberTicketRepository;
import com.festago.dto.EntryCodeResponse;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntryService {

    private final EntryCodeProvider entryCodeProvider;
    private final MemberTicketRepository memberTicketRepository;

    public EntryService(EntryCodeProvider entryCodeProvider, MemberTicketRepository memberTicketRepository) {
        this.entryCodeProvider = entryCodeProvider;
        this.memberTicketRepository = memberTicketRepository;
    }

    public EntryCodeResponse createEntryCode(Long memberId, Long memberTicketId) {
        MemberTicket memberTicket = memberTicketRepository.findById(memberTicketId)
                .orElseThrow(NoSuchElementException::new);
        if (!memberTicket.isOwner(memberId)) {
            throw new IllegalArgumentException(); // TODO
        }
        if (!memberTicket.canEntry(LocalDateTime.now())) {
            throw new IllegalArgumentException(); // TODO
        }
        EntryCode entryCode = EntryCode.create(entryCodeProvider, memberTicket);
        return EntryCodeResponse.of(entryCode);
    }
}
