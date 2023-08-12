package com.festago.presentation;

import com.festago.application.EntryService;
import com.festago.application.MemberTicketService;
import com.festago.application.TicketService;
import com.festago.auth.domain.Login;
import com.festago.auth.dto.LoginMember;
import com.festago.dto.EntryCodeResponse;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import com.festago.dto.TicketingRequest;
import com.festago.dto.TicketingResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerAuth")
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
    public ResponseEntity<EntryCodeResponse> createQR(
        @Login LoginMember loginMember,
        @PathVariable Long memberTicketId) {
        EntryCodeResponse response = entryService.createEntryCode(loginMember.memberId(), memberTicketId);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping
    public ResponseEntity<TicketingResponse> ticketing(
        @Login LoginMember loginMember,
        @RequestBody TicketingRequest request) {
        TicketingResponse response = ticketService.ticketing(loginMember.memberId(), request);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/{memberTicketId}")
    public ResponseEntity<MemberTicketResponse> findById(
        @Login LoginMember loginMember,
        @PathVariable Long memberTicketId) {
        MemberTicketResponse response = memberTicketService.findById(loginMember.memberId(), memberTicketId);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping
    public ResponseEntity<MemberTicketsResponse> findAll(
        @Login LoginMember loginMember,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("entryTime").descending());
        MemberTicketsResponse response = memberTicketService.findAll(loginMember.memberId(), pageRequest);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/current")
    public ResponseEntity<MemberTicketsResponse> findCurrent(@Login LoginMember loginMember) {
        Pageable pageable = PageRequest.of(0, 100);
        MemberTicketsResponse response = memberTicketService.findCurrent(loginMember.memberId(), pageable);
        return ResponseEntity.ok()
            .body(response);
    }
}
