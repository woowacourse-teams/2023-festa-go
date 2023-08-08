package com.festago.presentation;

import com.festago.application.MemberService;
import com.festago.dto.MemberResponse;
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
    public ResponseEntity<MemberResponse> findMemberInfo() {
        Long memberId = 1L;
        MemberResponse response = memberService.findMemberInfo(memberId);
        return ResponseEntity.ok()
            .body(response);
    }
}


