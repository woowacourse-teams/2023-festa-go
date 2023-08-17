package com.festago.presentation;

import com.festago.application.EntryService;
import com.festago.application.MemberTicketService;
import com.festago.application.TicketingService;
import com.festago.auth.annotation.Member;
import com.festago.dto.EntryCodeResponse;
import com.festago.dto.MemberTicketResponse;
import com.festago.dto.MemberTicketsResponse;
import com.festago.dto.TicketingRequest;
import com.festago.dto.TicketingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "유저 티켓 요청")
public class MemberTicketController {

    private final EntryService entryService;
    private final MemberTicketService memberTicketService;
    private final TicketingService ticketingService;

    public MemberTicketController(EntryService entryService, MemberTicketService memberTicketService,
                                  TicketingService ticketingService) {
        this.entryService = entryService;
        this.memberTicketService = memberTicketService;
        this.ticketingService = ticketingService;
    }

    @PostMapping("/{memberTicketId}/qr")
    @Operation(description = "티켓 제시용 QR 코드를 생성한다.")
    public ResponseEntity<EntryCodeResponse> createQR(@Member Long memberId,
                                                      @PathVariable Long memberTicketId) {
        EntryCodeResponse response = entryService.createEntryCode(memberId, memberTicketId);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping
    @Operation(description = "티켓을 예매한다.")
    public ResponseEntity<TicketingResponse> ticketing(@Member Long memberId,
                                                       @RequestBody TicketingRequest request) {
        TicketingResponse response = ticketingService.ticketing(memberId, request);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/{memberTicketId}")
    @Operation(description = "로그인한 맴버의 특정 티켓을 조회한다.")
    public ResponseEntity<MemberTicketResponse> findById(@Member Long memberId,
                                                         @PathVariable Long memberTicketId) {
        MemberTicketResponse response = memberTicketService.findById(memberId, memberTicketId);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping
    @Operation(description = "유저가 가진 모든 티켓을 조회한다.")
    public ResponseEntity<MemberTicketsResponse> findAll(@Member Long memberId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "100") int size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("entryTime").descending());
        MemberTicketsResponse response = memberTicketService.findAll(memberId, pageRequest);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/current")
    @Operation(description = "유저의 티켓 중 입장 시간이 24시간이상 지나지 않은 티켓을 현재 시간에 가까운 순서대로 입장 가능, 입장 예정 티켓으로 구분하여 반환하다.")
    public ResponseEntity<MemberTicketsResponse> findCurrent(@Member Long memberId) {
        Pageable pageable = PageRequest.of(0, 100);
        MemberTicketsResponse response = memberTicketService.findCurrent(memberId, pageable);
        return ResponseEntity.ok()
            .body(response);
    }
}
