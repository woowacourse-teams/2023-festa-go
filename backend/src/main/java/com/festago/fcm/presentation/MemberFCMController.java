package com.festago.fcm.presentation;

import com.festago.auth.annotation.Member;
import com.festago.fcm.application.MemberFCMService;
import com.festago.fcm.dto.MemberFcmCreateRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated(forRemoval = true)
@Hidden
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 FCM 정보 요청")
@RequestMapping("/member-fcm")
@SecurityRequirement(name = "bearerAuth")
public class MemberFCMController {

    private final MemberFCMService memberFCMService;

    @PostMapping
    @Operation(description = "유저의 FCM 토큰을 등록한다.", summary = "FCM 토큰 등록")
    public ResponseEntity<Void> createMemberFcm(@Member Long memberId,
                                                @RequestBody MemberFcmCreateRequest request) {
        memberFCMService.saveMemberFCM(memberId, request.fcmToken());
        return ResponseEntity.ok()
            .build();
    }
}
