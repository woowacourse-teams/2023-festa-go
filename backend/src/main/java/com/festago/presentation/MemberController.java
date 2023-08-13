package com.festago.presentation;

import com.festago.application.MemberService;
import com.festago.auth.domain.Login;
import com.festago.auth.dto.LoginMember;
import com.festago.dto.MemberProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(description = "현재 로그인한 유저의 프로필 정보를 조회한다.")
    public ResponseEntity<MemberProfileResponse> findMemberProfile(@Login LoginMember loginMember) {
        MemberProfileResponse response = memberService.findMemberProfile(loginMember.memberId());
        return ResponseEntity.ok()
            .body(response);
    }
}


