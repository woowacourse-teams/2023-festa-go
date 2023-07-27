package com.festago.presentation;

import com.festago.application.TicketService;
import com.festago.dto.StageTicketsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stages")
public class StageController {

    private final TicketService ticketService;

    public StageController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{stageId}/tickets")
    public ResponseEntity<StageTicketsResponse> findStageTickets(@PathVariable Long stageId) {
        StageTicketsResponse response = ticketService.findStageTickets(stageId);
        return ResponseEntity.ok()
            .body(response);
    }
}
