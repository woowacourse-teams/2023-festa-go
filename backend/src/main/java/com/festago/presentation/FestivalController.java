package com.festago.presentation;

import com.festago.application.FestivalService;
import com.festago.dto.FestivalDetailResponse;
import com.festago.dto.FestivalsResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Operation(description = "모든 축제들을 조회한다.")
    public ResponseEntity<FestivalsResponse> findAll() {
        FestivalsResponse response = festivalService.findAll();
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/{festivalId}")
    @Operation(description = "해당 Id 의 축제를 조회한다.")
    public ResponseEntity<FestivalDetailResponse> findDetail(@PathVariable Long festivalId) {
        FestivalDetailResponse response = festivalService.findDetail(festivalId);
        return ResponseEntity.ok()
            .body(response);
    }
}
