package com.festago.festival.presentation;

import com.festago.festival.application.FestivalService;
import com.festago.festival.dto.DetailFestivalResponse;
import com.festago.festival.dto.FestivalsResponse;
import com.festago.festival.repository.FestivalFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @deprecated 새로운 Festival CRUD 기능이 안정되면 삭제
 */
@Deprecated(forRemoval = true)
@RestController
@RequestMapping("/festivals")
@Tag(name = "축제 정보 요청")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping
    @Operation(description = "축제를 조건별로 조회한다. PROGRESS: 진행 중, PLANNED: 진행 예정, END: 종료, 기본값 -> 진행 중", summary = "축제 목록 조회")
    public ResponseEntity<FestivalsResponse> findFestivals(
        @RequestParam(defaultValue = "PROGRESS") String festivalFilter) {
        FestivalsResponse response = festivalService.findFestivals(FestivalFilter.from(festivalFilter));
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/{festivalId}")
    @Operation(description = "해당 Id 의 축제를 조회한다.", summary = "축제 상세 정보 조회")
    public ResponseEntity<DetailFestivalResponse> findDetail(@PathVariable Long festivalId) {
        DetailFestivalResponse response = festivalService.findDetail(festivalId);
        return ResponseEntity.ok()
            .body(response);
    }
}
