package com.festago.presentation;

import com.festago.application.MemberService;
import com.festago.dto.MemberProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> findMemberProfile() {
        Long memberId = 1L;
        MemberProfileResponse response = memberService.findMemberProfile(memberId);
        return ResponseEntity.ok()
            .body(response);
    }
}


