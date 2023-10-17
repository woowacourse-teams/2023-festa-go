package com.festago.presentation;


import com.festago.entry.application.EntryService;
import com.festago.entry.dto.TicketValidationRequest;
import com.festago.entry.dto.TicketValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff/member-tickets")
@Tag(name = "스태프 요청")
@RequiredArgsConstructor
public class StaffMemberTicketController {

    private final EntryService entryService;

    @PostMapping("/validation")
    @Operation(description = "스태프가 티켓을 검사한다.", summary = "티켓 검사")
    public ResponseEntity<TicketValidationResponse> validate(@RequestBody @Valid TicketValidationRequest request) {
        TicketValidationResponse response = entryService.validate(request);
        return ResponseEntity.ok()
            .body(response);
    }
}
