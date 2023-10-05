package com.festago.presentation;

import com.festago.admin.application.AdminService;
import com.festago.admin.dto.AdminResponse;
import com.festago.auth.annotation.Admin;
import com.festago.auth.application.AdminAuthService;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.AdminSignupRequest;
import com.festago.auth.dto.AdminSignupResponse;
import com.festago.auth.dto.RootAdminInitializeRequest;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.festival.application.FestivalService;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.FestivalResponse;
import com.festago.festival.dto.FestivalUpdateRequest;
import com.festago.school.application.SchoolService;
import com.festago.school.dto.SchoolCreateRequest;
import com.festago.school.dto.SchoolResponse;
import com.festago.school.dto.SchoolUpdateRequest;
import com.festago.stage.application.StageService;
import com.festago.stage.dto.StageCreateRequest;
import com.festago.stage.dto.StageResponse;
import com.festago.stage.dto.StageUpdateRequest;
import com.festago.ticket.application.TicketService;
import com.festago.ticket.dto.TicketCreateRequest;
import com.festago.ticket.dto.TicketCreateResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api")
@Hidden
@RequiredArgsConstructor
public class AdminController {

    private final FestivalService festivalService;
    private final StageService stageService;
    private final TicketService ticketService;
    private final AdminService adminService;
    private final AdminAuthService adminAuthService;
    private final SchoolService schoolService;
    private final Optional<BuildProperties> properties;

    @PostMapping("/festivals")
    public ResponseEntity<FestivalResponse> createFestival(@RequestBody @Valid FestivalCreateRequest request) {
        FestivalResponse response = festivalService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PatchMapping("/festivals/{festivalId}")
    public ResponseEntity<Void> updateFestival(@RequestBody @Valid FestivalUpdateRequest request,
                                               @PathVariable Long festivalId) {
        festivalService.update(festivalId, request);
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("/festivals/{festivalId}")
    public ResponseEntity<Void> deleteFestival(@PathVariable Long festivalId) {
        festivalService.delete(festivalId);
        return ResponseEntity.ok()
            .build();
    }

    @PostMapping("/stages")
    public ResponseEntity<StageResponse> createStage(@RequestBody @Valid StageCreateRequest request) {
        StageResponse response = stageService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PatchMapping("/stages/{stageId}")
    public ResponseEntity<Void> updateStage(@RequestBody @Valid StageUpdateRequest request,
                                            @PathVariable Long stageId) {
        stageService.update(stageId, request);
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("/stages/{stageId}")
    public ResponseEntity<Void> deleteStage(@PathVariable Long stageId) {
        stageService.delete(stageId);
        return ResponseEntity.ok()
            .build();
    }

    @PostMapping("/tickets")
    public ResponseEntity<TicketCreateResponse> createTicket(@RequestBody @Valid TicketCreateRequest request) {
        TicketCreateResponse response = ticketService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping("/schools")
    public ResponseEntity<SchoolResponse> createSchool(@RequestBody @Valid SchoolCreateRequest request) {
        SchoolResponse response = schoolService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PatchMapping("/schools/{schoolId}")
    public ResponseEntity<Void> updateSchool(@RequestBody @Valid SchoolUpdateRequest request,
                                             @PathVariable Long schoolId) {
        schoolService.update(schoolId, request);
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("/schools/{schoolId}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long schoolId) {
        schoolService.delete(schoolId);
        return ResponseEntity.ok()
            .build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid AdminLoginRequest request) {
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

    @GetMapping("/info")
    public ResponseEntity<Void> getInfo() {
        throw new BadRequestException(ErrorCode.FOR_TEST_ERROR);
    }

    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeRootAdmin(@RequestBody @Valid RootAdminInitializeRequest request) {
        adminAuthService.initializeRootAdmin(request.password());
        return ResponseEntity.ok()
            .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<AdminSignupResponse> signupAdminAccount(@RequestBody @Valid AdminSignupRequest request,
                                                                  @Admin Long adminId) {
        AdminSignupResponse response = adminAuthService.signup(adminId, request);
        return ResponseEntity.ok()
            .body(response);
    }
}
