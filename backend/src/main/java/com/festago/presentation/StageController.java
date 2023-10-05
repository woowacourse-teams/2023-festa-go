package com.festago.presentation;

import com.festago.ticket.application.TicketService;
import com.festago.ticket.dto.StageTicketsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stages")
@Tag(name = "공연 정보 요청")
@RequiredArgsConstructor
public class StageController {

    private final TicketService ticketService;

    @GetMapping("/{stageId}/tickets")
    @Operation(description = "특정 무대의 티켓 정보를 보여준다.", summary = "무대 티켓 목록 조회")
    public ResponseEntity<StageTicketsResponse> findStageTickets(@PathVariable Long stageId) {
        StageTicketsResponse response = ticketService.findStageTickets(stageId);
        return ResponseEntity.ok()
            .body(response);
    }
}
