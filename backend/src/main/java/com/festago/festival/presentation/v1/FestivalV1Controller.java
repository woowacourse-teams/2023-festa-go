package com.festago.festival.presentation.v1;

import com.festago.common.aop.ValidPageable;
import com.festago.common.exception.ValidException;
import com.festago.festival.application.FestivalDetailV1QueryService;
import com.festago.festival.application.FestivalV1QueryService;
import com.festago.festival.dto.FestivalDetailV1Response;
import com.festago.festival.dto.FestivalV1QueryRequest;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.repository.FestivalFilter;
import com.festago.school.domain.SchoolRegion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/festivals")
@Tag(name = "축제 정보 요청 V1")
@RequiredArgsConstructor
public class FestivalV1Controller {

    private final FestivalV1QueryService festivalV1QueryService;
    private final FestivalDetailV1QueryService festivalDetailV1QueryService;

    @GetMapping
    @ValidPageable(maxSize = 20)
    @Operation(description = "축제 목록를 조건별로 조회한다.", summary = "축제 목록 조회")
    public ResponseEntity<Slice<FestivalV1Response>> findFestivals(
        @Parameter(description = "0 < size <= 20") @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "ANY") SchoolRegion region,
        @Parameter(description = "PROGRESS: 진행 중, PLANNED: 진행 예정, END: 종료") @RequestParam(defaultValue = "PROGRESS") FestivalFilter filter,
        @RequestParam(required = false) Long lastFestivalId,
        @RequestParam(required = false) LocalDate lastStartDate
    ) {
        validateCursor(lastFestivalId, lastStartDate);
        var request = new FestivalV1QueryRequest(region, filter, lastFestivalId, lastStartDate);
        var response = festivalV1QueryService.findFestivals(PageRequest.ofSize(size), request);
        return ResponseEntity.ok(response);
    }

    private void validateCursor(Long lastFestivalId, LocalDate lastStartDate) {
        if (lastFestivalId == null && lastStartDate == null) {
            return;
        }
        if (lastFestivalId != null && lastStartDate != null) {
            return;
        }
        throw new ValidException("festivalId, lastStartDate 두 값 모두 요청하거나 요청하지 않아야합니다.");
    }

    @GetMapping("/{festivalId}")
    @Operation(description = "축제의 정보를 조회한다.", summary = "축제 정보 조회")
    public ResponseEntity<FestivalDetailV1Response> findFestivalDetail(
        @PathVariable Long festivalId
    ) {
        var response = festivalDetailV1QueryService.findFestivalDetail(festivalId);
        return ResponseEntity.ok(response);
    }
}
