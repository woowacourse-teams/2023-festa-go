package com.festago.presentation;


import com.festago.application.EntryService;
import com.festago.dto.TicketValidationRequest;
import com.festago.dto.TicketValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff/tickets")
public class StaffTicketController {

    private final EntryService entryService;

    public StaffTicketController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping("/validation")
    public ResponseEntity<TicketValidationResponse> validate(@RequestBody TicketValidationRequest request) {
        TicketValidationResponse response = entryService.validate(request);
        return ResponseEntity.ok()
            .body(response);
    }
}
