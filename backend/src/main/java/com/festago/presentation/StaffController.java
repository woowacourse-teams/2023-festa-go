package com.festago.presentation;

import com.festago.auth.application.StaffAuthService;
import com.festago.auth.dto.StaffLoginRequest;
import com.festago.auth.dto.StaffLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
@Tag(name = "스태프 요청")
public class StaffController {

    private final StaffAuthService staffAuthService;

    public StaffController(StaffAuthService staffAuthService) {
        this.staffAuthService = staffAuthService;
    }

    @PostMapping("/login")
    @Operation(description = "스태프 코드로 로그인한다.", summary = "스태프 로그인")
    public ResponseEntity<StaffLoginResponse> login(@RequestBody StaffLoginRequest request) {
        StaffLoginResponse response = staffAuthService.login(request);
        return ResponseEntity.ok()
            .body(response);
    }
}
