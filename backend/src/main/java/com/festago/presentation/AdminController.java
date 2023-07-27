package com.festago.presentation;

import com.festago.application.FestivalService;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
public class AdminController {

    private final FestivalService festivalService;

    public AdminController(FestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @PostMapping("/festivals")
    public ResponseEntity<FestivalResponse> createFestival(@RequestBody FestivalCreateRequest request) {
        return ResponseEntity.ok()
            .body(festivalService.create(request));
    }
}
