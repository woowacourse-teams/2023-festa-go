package com.festago.presentation;

import com.festago.application.FestivalService;
import com.festago.dto.FestivalsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/festivals")
public class FestivalController {

    private final FestivalService festivalService;

    public FestivalController(FestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @GetMapping
    public ResponseEntity<FestivalsResponse> findAll() {
        FestivalsResponse response = festivalService.findAll();
        return ResponseEntity.ok()
            .body(response);
    }
}
