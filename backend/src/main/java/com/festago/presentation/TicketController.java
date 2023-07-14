package com.festago.presentation;

import com.festago.application.EntryService;
import com.festago.dto.EntryCodeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final EntryService entryService;

    public TicketController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping("/{memberTicketId}/qr")
    public ResponseEntity<EntryCodeResponse> createQR(@PathVariable Long memberTicketId) {
        EntryCodeResponse response =  entryService.createEntryCode(1L, memberTicketId); // TODO
        return ResponseEntity.ok()
            .body(response);
    }
}
