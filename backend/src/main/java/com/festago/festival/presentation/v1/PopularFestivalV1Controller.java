package com.festago.festival.presentation.v1;

import com.festago.festival.application.PopularFestivalV1QueryService;
import com.festago.festival.dto.PopularFestivalsV1Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/popular/festivals")
@Tag(name = "인기 축제 정보 요청 V1")
@RequiredArgsConstructor
public class PopularFestivalV1Controller {

    private final PopularFestivalV1QueryService popularFestivalV1QueryService;

    @GetMapping
    @Operation(description = "인기 축제 목록을 7개 반환한다.", summary = "인기 축제 목록 조회")
    public ResponseEntity<PopularFestivalsV1Response> popularFestivals() {
        return ResponseEntity.ok(popularFestivalV1QueryService.findPopularFestivals());
    }
}
