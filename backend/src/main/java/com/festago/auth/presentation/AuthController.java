package com.festago.auth.presentation;

import com.festago.auth.annotation.Member;
import com.festago.auth.application.AuthService;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/oauth2")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@Member Long memberId) {
        authService.deleteMember(memberId);
        return ResponseEntity.ok()
            .build();
    }
}
