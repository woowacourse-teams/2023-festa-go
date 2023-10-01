package com.festago.presentation;

import com.festago.auth.annotation.Staff;
import com.festago.auth.application.StaffAuthService;
import com.festago.auth.dto.StaffLoginRequest;
import com.festago.auth.dto.StaffLoginResponse;
import com.festago.entry.application.EntryService;
import com.festago.entry.dto.TicketValidationRequest;
import com.festago.entry.dto.TicketValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final EntryService entryService;


    public StaffController(StaffAuthService staffAuthService, EntryService entryService) {
        this.staffAuthService = staffAuthService;
        this.entryService = entryService;
    }

    @PostMapping("/login")
    @Operation(description = "스태프 코드로 로그인한다.", summary = "스태프 로그인")
    public ResponseEntity<StaffLoginResponse> login(@RequestBody @Valid StaffLoginRequest request) {
        StaffLoginResponse response = staffAuthService.login(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping("/member-tickets/validation")
    @Operation(description = "스태프가 티켓을 검사한다.", summary = "티켓 검사")
    public ResponseEntity<TicketValidationResponse> validate(
        @RequestBody @Valid TicketValidationRequest request,
        @Staff Long staffId) {
        TicketValidationResponse response = entryService.validate(request, staffId);
        return ResponseEntity.ok()
            .body(response);
    }
}
