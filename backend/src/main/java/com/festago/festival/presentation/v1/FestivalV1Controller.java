package com.festago.festival.presentation.v1;

import com.festago.common.aop.ValidPageable;
import com.festago.common.exception.ValidException;
import com.festago.festival.application.FestivalV1QueryService;
import com.festago.festival.dto.FestivalV1QueryRequest;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.repository.FestivalFilter;
import com.festago.school.domain.SchoolRegion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/festivals")
@Tag(name = "축제 정보 요청 V1")
@RequiredArgsConstructor
public class FestivalV1Controller {

    private final FestivalV1QueryService festivalV1QueryService;

    @GetMapping
    @ValidPageable(maxSize = 20)
    @Operation(description = "축제 목록를 조건별로 조회한다. PROGRESS: 진행 중, PLANNED: 진행 예정, END: 종료, 기본값 -> 진행 중, limit의 크기는 0 < limit < 21 이며 기본 값 10이다.", summary = "축제 목록 조회")
    public ResponseEntity<Slice<FestivalV1Response>> findFestivals(
        @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(defaultValue = "ANY") SchoolRegion region,
        @RequestParam(defaultValue = "PROGRESS") FestivalFilter filter,
        @RequestParam(required = false) Long lastFestivalId,
        @RequestParam(required = false) LocalDate lastStartDate
    ) {
        validateCursor(lastFestivalId, lastStartDate);
        var request = new FestivalV1QueryRequest(region, filter, lastFestivalId, lastStartDate);
        var response = festivalV1QueryService.findFestivals(pageable, request);
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
}
