package com.festago.auth.presentation;

import com.festago.auth.application.AuthService;
import com.festago.auth.domain.SocialType;
import com.festago.auth.dto.LoginRequest;
import com.festago.auth.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/oauth2/kakao")
    public ResponseEntity<LoginResponse> loginWithKakao(@RequestParam String code) {
        LoginResponse response = authService.login(new LoginRequest(SocialType.KAKAO, code));
        return ResponseEntity.ok()
            .body(response);
    }
}
