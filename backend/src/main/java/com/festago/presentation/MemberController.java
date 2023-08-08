package com.festago.presentation;

import com.festago.application.MemberService;
import com.festago.application.MemberTicketService;
import com.festago.dto.MemberResponse;
import com.festago.dto.MemberTicketResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberTicketService memberTicketService;

    public MemberController(MemberService memberService, MemberTicketService memberTicketService) {
        this.memberService = memberService;
        this.memberTicketService = memberTicketService;
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberResponse> findMemberInfo() {
        Long memberId = 1L;
        MemberResponse response = memberService.findMemberInfo(memberId);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/ticket")
    public ResponseEntity<MemberTicketResponse> findRecentTicket() {
        Long memberId = 1L;
        MemberTicketResponse response = memberTicketService.findRecentlyReservedTicket(memberId);
        return ResponseEntity.ok()
            .body(response);
    }
}


