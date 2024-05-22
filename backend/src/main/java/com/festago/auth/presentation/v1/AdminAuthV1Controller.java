package com.festago.auth.presentation.v1;

import com.festago.auth.application.command.AdminAuthCommandService;
import com.festago.auth.domain.authentication.AdminAuthentication;
import com.festago.auth.dto.AdminLoginV1Request;
import com.festago.auth.dto.AdminLoginV1Response;
import com.festago.auth.dto.AdminSignupV1Request;
import com.festago.auth.dto.RootAdminInitializeRequest;
import com.festago.auth.dto.command.AdminLoginResult;
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
@RequestMapping("/admin/api/v1/auth")
@Hidden
@RequiredArgsConstructor
public class AdminAuthV1Controller {

    private final AdminAuthCommandService adminAuthCommandService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginV1Response> login(
        @RequestBody @Valid AdminLoginV1Request request
    ) {
        AdminLoginResult result = adminAuthCommandService.login(request.toCommand());
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, createLoginCookie(result.accessToken()))
            .body(new AdminLoginV1Response(result.username(), result.authType()));
    }

    private String createLoginCookie(String token) {
        return ResponseCookie.from("token", token)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .build().toString();
    }

    @GetMapping("/logout")
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
    public ResponseEntity<Void> signupAdminAccount(
        @RequestBody @Valid AdminSignupV1Request request,
        AdminAuthentication adminAuthentication
    ) {
        adminAuthCommandService.signup(adminAuthentication.getId(), request.toCommand());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeRootAdmin(
        @RequestBody @Valid RootAdminInitializeRequest request
    ) {
        adminAuthCommandService.initializeRootAdmin(request.password());
        return ResponseEntity.ok().build();
    }
}
