package com.festago.presentation;

import com.festago.application.FestivalService;
import com.festago.application.StageService;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalResponse;
import com.festago.dto.StageCreateRequest;
import com.festago.dto.StageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final FestivalService festivalService;
    private final StageService stageService;

    public AdminController(FestivalService festivalService, StageService stageService) {
        this.festivalService = festivalService;
        this.stageService = stageService;
    }

    @PostMapping("/festivals")
    public ResponseEntity<FestivalResponse> createFestival(@RequestBody FestivalCreateRequest request) {
        FestivalResponse response = festivalService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping("/stages")
    public ResponseEntity<StageResponse> createFestival(@RequestBody StageCreateRequest request) {
        StageResponse response = stageService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }
}
