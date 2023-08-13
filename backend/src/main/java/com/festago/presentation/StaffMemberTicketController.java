package com.festago.presentation;


import com.festago.application.EntryService;
import com.festago.dto.TicketValidationRequest;
import com.festago.dto.TicketValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff/member-tickets")
@Tag(name = "스태프 요청")
public class StaffMemberTicketController {

    private final EntryService entryService;

    public StaffMemberTicketController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping("/validation")
    @Operation(description = "스태프가 티켓을 검사한다.")
    public ResponseEntity<TicketValidationResponse> validate(@RequestBody TicketValidationRequest request) {
        TicketValidationResponse response = entryService.validate(request);
        return ResponseEntity.ok()
            .body(response);
    }
}
