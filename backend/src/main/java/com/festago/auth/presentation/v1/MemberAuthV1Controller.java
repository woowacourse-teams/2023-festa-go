package com.festago.auth.presentation.v1;

import com.festago.auth.annotation.Member;
import com.festago.auth.annotation.MemberAuth;
import com.festago.auth.application.command.MemberAuthFacadeService;
import com.festago.auth.domain.SocialType;
import com.festago.auth.dto.v1.LoginV1Response;
import com.festago.auth.dto.v1.LogoutV1Request;
import com.festago.auth.dto.v1.OAuth2LoginV1Request;
import com.festago.auth.dto.v1.RefreshTokenV1Request;
import com.festago.auth.dto.v1.TokenRefreshV1Response;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "회원 인증 V1")
public class MemberAuthV1Controller {

    private final MemberAuthFacadeService memberAuthFacadeService;

    @PostMapping("/login/oauth2")
    @Operation(description = "OAuth2 authorization_code를 받아 로그인/회원가입을 한다.", summary = "OAuth2 Authorization Code Grant 로그인")
    public ResponseEntity<LoginV1Response> oauth2Login(
        @Valid @RequestBody OAuth2LoginV1Request request
    ) {
        return ResponseEntity.ok()
            .body(memberAuthFacadeService.oAuth2Login(request.socialType(), request.code()));
    }

    @Hidden // OAuth2 redirect-uri 스펙을 맞추기 위해 구현한 API
    @GetMapping("/login/oauth2/{socialType}")
    public ResponseEntity<LoginV1Response> oauth2LoginWithPath(
        @PathVariable SocialType socialType,
        @RequestParam String code
    ) {
        return ResponseEntity.ok()
            .body(memberAuthFacadeService.oAuth2Login(socialType, code));
    }

    @MemberAuth
    @PostMapping("/logout")
    @Operation(description = "로그인 된 사용자를 로그아웃 처리한다.", summary = "로그아웃")
    public ResponseEntity<Void> logout(
        @Member Long memberId,
        @RequestBody @Valid LogoutV1Request request
    ) {
        memberAuthFacadeService.logout(memberId, UUID.fromString(request.refreshToken()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    @Operation(description = "액세스/리프래쉬 토큰을 재발급한다.", summary = "액세스/리프래쉬 토큰 재발급")
    public ResponseEntity<TokenRefreshV1Response> refresh(
        @Valid @RequestBody RefreshTokenV1Request request
    ) {
        return ResponseEntity.ok()
            .body(memberAuthFacadeService.refresh(UUID.fromString(request.refreshToken())));
    }

    @MemberAuth
    @DeleteMapping
    @Operation(description = "사용자를 탈퇴 처리한다.", summary = "회원 탈퇴")
    public ResponseEntity<Void> deleteAccount(@Member Long memberId) {
        memberAuthFacadeService.deleteAccount(memberId);
        return ResponseEntity.ok().build();
    }
}
