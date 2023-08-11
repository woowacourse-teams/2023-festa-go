package com.festago.presentation;

import com.festago.application.AdminService;
import com.festago.application.FestivalService;
import com.festago.application.StageService;
import com.festago.application.TicketService;
import com.festago.dto.AdminResponse;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalResponse;
import com.festago.dto.StageCreateRequest;
import com.festago.dto.StageResponse;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketCreateResponse;
import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final FestivalService festivalService;
    private final StageService stageService;
    private final TicketService ticketService;
    private final AdminService adminService;
    private final Optional<BuildProperties> properties;

    public AdminController(FestivalService festivalService, StageService stageService, TicketService ticketService,
                           AdminService adminService, Optional<BuildProperties> buildProperties) {
        this.festivalService = festivalService;
        this.stageService = stageService;
        this.ticketService = ticketService;
        this.adminService = adminService;
        this.properties = buildProperties;
    }

    @PostMapping("/festivals")
    public ResponseEntity<FestivalResponse> createFestival(@RequestBody FestivalCreateRequest request) {
        FestivalResponse response = festivalService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping("/stages")
    public ResponseEntity<StageResponse> createStage(@RequestBody StageCreateRequest request) {
        StageResponse response = stageService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping("/tickets")
    public ResponseEntity<TicketCreateResponse> createTicket(@RequestBody TicketCreateRequest request) {
        TicketCreateResponse response = ticketService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping
    public ModelAndView adminPage() {
        return new ModelAndView("admin");
    }

    @GetMapping("/data")
    public ResponseEntity<AdminResponse> adminData() {
        AdminResponse response = adminService.getAdminResponse();
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/version")
    public ResponseEntity<String> getVersion() {
        return properties.map(it -> ResponseEntity.ok(it.getTime().atZone(ZoneId.of("Asia/Seoul")).toString()))
            .orElseGet(() -> ResponseEntity.ok()
                .body(LocalDateTime.now().toString()));
    }

    @GetMapping("/error")
    public ResponseEntity<Void> getError() {
        throw new IllegalArgumentException("테스트용 에러입니다.");
    }

    @GetMapping("/warn")
    public ResponseEntity<Void> getWarn() {
        throw new InternalServerException(ErrorCode.FOR_TEST_ERROR);
    }
}
