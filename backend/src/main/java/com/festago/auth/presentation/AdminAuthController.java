package com.festago.auth.presentation;

import com.festago.auth.annotation.Admin;
import com.festago.auth.application.AdminAuthService;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.AdminSignupRequest;
import com.festago.auth.dto.AdminSignupResponse;
import com.festago.auth.dto.RootAdminInitializeRequest;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api")
@Hidden
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    @Hidden
    public ResponseEntity<Void> login(@RequestBody @Valid AdminLoginRequest request) {
        String token = adminAuthService.login(request);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, createLoginCookie(token))
            .build();
    }

    private String createLoginCookie(String token) {
        return ResponseCookie.from("token", token)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .build().toString();
    }

    /**
     * 클라이언트 측에서 httpOnly 쿠키를 조작할 수 없기 때문에, 서버 측에서 쿠키를 관리해주어야 함
     */
    @GetMapping("/logout")
    @Hidden
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, createLogoutCookie())
            .build();
    }

    private String createLogoutCookie() {
        return ResponseCookie.from("token", "")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ZERO)
            .build().toString();
    }

    @PostMapping("/signup")
    @Hidden
    public ResponseEntity<AdminSignupResponse> signupAdminAccount(@RequestBody @Valid AdminSignupRequest request,
                                                                  @Admin Long adminId) {
        AdminSignupResponse response = adminAuthService.signup(adminId, request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping("/initialize")
    @Hidden
    public ResponseEntity<Void> initializeRootAdmin(@RequestBody @Valid RootAdminInitializeRequest request) {
        adminAuthService.initializeRootAdmin(request.password());
        return ResponseEntity.ok()
            .build();
    }
}
