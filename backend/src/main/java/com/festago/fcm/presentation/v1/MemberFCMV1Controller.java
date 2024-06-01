package com.festago.fcm.presentation.v1;

import com.festago.auth.annotation.MemberAuth;
import com.festago.auth.domain.authentication.MemberAuthentication;
import com.festago.fcm.application.command.MemberFCMCommandService;
import com.festago.fcm.dto.v1.MemberFCMTokenRegisterV1Request;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class MemberFCMV1Controller {

    private final MemberFCMCommandService memberFCMCommandService;

    @MemberAuth
    @PostMapping
    public ResponseEntity<Void> registerFCM(
        MemberAuthentication memberAuthentication,
        @RequestBody MemberFCMTokenRegisterV1Request request
    ) {
        memberFCMCommandService.registerFCM(memberAuthentication.getId(), request.fcmToken());
        return ResponseEntity.ok().build();
    }
}
