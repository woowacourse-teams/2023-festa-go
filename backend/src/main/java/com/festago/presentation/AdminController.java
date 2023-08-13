package com.festago.presentation;

import com.festago.application.AdminService;
import com.festago.application.FestivalService;
import com.festago.application.StageService;
import com.festago.application.TicketService;
import com.festago.auth.application.AdminAuthService;
import com.festago.auth.dto.AdminInitializeRequest;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.AdminSignupRequest;
import com.festago.auth.dto.AdminSignupResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private final AdminAuthService adminAuthService;
    private final Optional<BuildProperties> properties;

    public AdminController(FestivalService festivalService, StageService stageService, TicketService ticketService,
                           AdminService adminService, AdminAuthService adminAuthService,
                           Optional<BuildProperties> buildProperties) {
        this.festivalService = festivalService;
        this.stageService = stageService;
        this.ticketService = ticketService;
        this.adminService = adminService;
        this.adminAuthService = adminAuthService;
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
        return new ModelAndView("admin/admin-page");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("admin/login");
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AdminLoginRequest request) {
        String token = adminAuthService.login(request);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, getCookie(token))
            .build();
    }

    private String getCookie(String token) {
        return ResponseCookie.from("token", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .build().toString();
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

    @PostMapping("/initialize")
    public ResponseEntity<Void> initialFirstAdmin(@RequestBody AdminInitializeRequest request) {
        adminAuthService.initialFirstAdminAccount(request.password());
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/signup") // TODO 추후 최초 admin 계정만 접속하도록
    public ModelAndView signupPage() {
        return new ModelAndView("admin/signup");
    }

    @PostMapping("/signup") // TODO 추후 최초 admin 계정만 접속하도록
    public ResponseEntity<AdminSignupResponse> signupAdminAccount(@RequestBody AdminSignupRequest request) {
        AdminSignupResponse response = adminAuthService.signup(request);
        return ResponseEntity.ok()
            .body(response);
    }
}
