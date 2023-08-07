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

    public AdminController(FestivalService festivalService, StageService stageService, TicketService ticketService,
                           AdminService adminService) {
        this.festivalService = festivalService;
        this.stageService = stageService;
        this.ticketService = ticketService;
        this.adminService = adminService;
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
}
