package com.festago.presentation;

import com.festago.application.EntryService;
import com.festago.application.MemberTicketService;
import com.festago.dto.EntryCodeResponse;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final EntryService entryService;
    private final MemberTicketService memberTicketService;

    public TicketController(EntryService entryService, MemberTicketService memberTicketService) {
        this.entryService = entryService;
        this.memberTicketService = memberTicketService;
    }

    @PostMapping("/{memberTicketId}/qr")
    public ResponseEntity<EntryCodeResponse> createQR(@PathVariable Long memberTicketId) {
        EntryCodeResponse response = entryService.createEntryCode(1L, memberTicketId); // TODO
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/{memberTicketId}")
    public ResponseEntity<MemberTicketResponse> findById(@PathVariable Long memberTicketId) {
        MemberTicketResponse response = memberTicketService.findById(1L, memberTicketId);// TODO
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping
    public ResponseEntity<MemberTicketsResponse> findAll() {
        MemberTicketsResponse response = memberTicketService.findAll(1L);// TODO
        return ResponseEntity.ok()
            .body(response);
    }
}
