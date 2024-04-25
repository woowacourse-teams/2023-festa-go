package com.festago.auth.presentation.v1;

import com.festago.auth.annotation.Member;
import com.festago.auth.annotation.MemberAuth;
import com.festago.auth.application.command.MemberAuthFacadeService;
import com.festago.auth.dto.v1.LoginV1Response;
import com.festago.auth.dto.v1.LogoutV1Request;
import com.festago.auth.dto.v1.OAuth2LoginV1Request;
import com.festago.auth.dto.v1.RefreshTokenV1Request;
import com.festago.auth.dto.v1.TokenRefreshV1Response;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MemberAuthV1Controller {

    private final MemberAuthFacadeService memberAuthFacadeService;

    @PostMapping("/login/oauth2")
    public ResponseEntity<LoginV1Response> oauth2Login(
        @Valid @RequestBody OAuth2LoginV1Request request
    ) {
        return ResponseEntity.ok()
            .body(memberAuthFacadeService.oAuth2Login(request.socialType(), request.code()));
    }

    @MemberAuth
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
        @Member Long memberId,
        @RequestBody @Valid LogoutV1Request request
        ) {
        memberAuthFacadeService.logout(memberId, UUID.fromString(request.refreshToken()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshV1Response> refresh(
        @Valid @RequestBody RefreshTokenV1Request request
    ) {
        return ResponseEntity.ok()
            .body(memberAuthFacadeService.refresh(UUID.fromString(request.refreshToken())));
    }

    @MemberAuth
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@Member Long memberId) {
        memberAuthFacadeService.deleteAccount(memberId);
        return ResponseEntity.ok().build();
    }
}
