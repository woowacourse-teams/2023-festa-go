package com.festago.presentation;

import com.festago.application.EntryService;
import com.festago.application.MemberTicketService;
import com.festago.application.TicketService;
import com.festago.dto.CurrentMemberTicketsResponse;
import com.festago.dto.EntryCodeResponse;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import com.festago.dto.TicketingRequest;
import com.festago.dto.TicketingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member-tickets")
public class MemberTicketController {

    private final EntryService entryService;
    private final MemberTicketService memberTicketService;
    private final TicketService ticketService;

    public MemberTicketController(EntryService entryService, MemberTicketService memberTicketService,
                                  TicketService ticketService) {
        this.entryService = entryService;
        this.memberTicketService = memberTicketService;
        this.ticketService = ticketService;
    }

    @PostMapping("/{memberTicketId}/qr")
    public ResponseEntity<EntryCodeResponse> createQR(@PathVariable Long memberTicketId) {
        EntryCodeResponse response = entryService.createEntryCode(1L, memberTicketId); // TODO
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping
    public ResponseEntity<TicketingResponse> ticketing(@RequestBody TicketingRequest request) {
        TicketingResponse response = ticketService.ticketing(1L, request);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/{memberTicketId}")
    public ResponseEntity<MemberTicketResponse> findById(@PathVariable Long memberTicketId) {
        MemberTicketResponse response = memberTicketService.findById(1L, memberTicketId); // TODO
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping
    public ResponseEntity<MemberTicketsResponse> findAll() {
        MemberTicketsResponse response = memberTicketService.findAll(1L);// TODO
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/current")
    public ResponseEntity<CurrentMemberTicketsResponse> findCurrent() {
        CurrentMemberTicketsResponse response = memberTicketService.findCurrent(1L);// TODO
        return ResponseEntity.ok()
            .body(response);
    }
}
