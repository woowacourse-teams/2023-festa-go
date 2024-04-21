package com.festago.auth.presentation.v1;

import com.festago.auth.annotation.Member;
import com.festago.auth.annotation.MemberAuth;
import com.festago.auth.dto.v1.OAuth2LoginV1Request;
import com.festago.auth.dto.v1.RefreshTokenV1Request;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MemberAuthV1Controller {

    @GetMapping("/login/oauth2")
    public ResponseEntity<Void> oauth2Login(
        @Valid @RequestBody OAuth2LoginV1Request request
    ) {
        return ResponseEntity.ok().build();
    }

    @MemberAuth
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@Member Long memberId) {
        return ResponseEntity.ok().build();
    }

    @MemberAuth
    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh(
        @Member Long memberId,
        @Valid @RequestBody RefreshTokenV1Request request
    ) {
        return ResponseEntity.ok().build();
    }

    @MemberAuth
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@Member Long memberId) {
        return ResponseEntity.ok().build();
    }
}
