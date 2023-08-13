package com.festago.auth.presentation;

import com.festago.auth.application.AuthService;
import com.festago.auth.domain.Login;
import com.festago.auth.dto.LoginMember;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "로그인 관련 요청")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/oauth2")
    @Operation(description = "엑세스 토큰을 기반으로 로그인 요청을 보낸다.")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @DeleteMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "회원 탈퇴 요청을 보낸다.")
    public ResponseEntity<Void> deleteMember(@Login LoginMember loginMember) {
        authService.deleteMember(loginMember.memberId());
        return ResponseEntity.ok()
            .build();
    }
}
