package com.festago.member.presentation;

import com.festago.auth.annotation.Member;
import com.festago.member.application.MemberService;
import com.festago.member.dto.MemberProfileResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated(forRemoval = true)
@Hidden
@RestController
@RequestMapping("/members")
@Tag(name = "유저 정보 요청")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/profile")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "현재 로그인한 유저의 프로필 정보를 조회한다.", summary = "사용자 정보 조회")
    public ResponseEntity<MemberProfileResponse> findMemberProfile(@Member Long memberId) {
        MemberProfileResponse response = memberService.findMemberProfile(memberId);
        return ResponseEntity.ok()
            .body(response);
    }
}


