package com.festago.entry.presentation;

import com.festago.auth.annotation.Member;
import com.festago.entry.application.EntryService;
import com.festago.entry.dto.EntryCodeResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated(forRemoval = true)
@Hidden
@RestController
@RequiredArgsConstructor
public class MemberEntranceController {

    private final EntryService entryService;

    @PostMapping("/member-tickets/{memberTicketId}/qr")
    @Operation(description = "티켓 제시용 QR 코드를 생성한다.", summary = "티켓 제시용 QR 생성")
    public ResponseEntity<EntryCodeResponse> createQR(@Member Long memberId,
                                                      @PathVariable Long memberTicketId) {
        EntryCodeResponse response = entryService.createEntryCode(memberId, memberTicketId);
        return ResponseEntity.ok()
            .body(response);
    }
}
