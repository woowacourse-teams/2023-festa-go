package com.festago.school.presentation.v1;

import com.festago.common.aop.ValidPageable;
import com.festago.common.dto.SliceResponse;
import com.festago.school.application.v1.SchoolV1QueryService;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalV1Response;
import com.festago.school.repository.v1.SchoolFestivalV1SearchCondition;
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
@RequestMapping("/api/v1/schools")
@Tag(name = "학교 정보 요청 V1")
@RequiredArgsConstructor
public class SchoolV1Controller {

    private final SchoolV1QueryService schoolV1QueryService;

    @GetMapping("/{schoolId}")
    @Operation(description = "학교의 정보를 조회한다.", summary = "학교 정보 조회")
    public ResponseEntity<SchoolDetailV1Response> findDetailById(@PathVariable Long schoolId) {
        SchoolDetailV1Response response = schoolV1QueryService.findDetailById(schoolId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{schoolId}/festivals")
    @ValidPageable(maxSize = 20)
    @Operation(description = "학교의 축제 목록을 조회한다.", summary = "학교 축제 목록 조회")
    public ResponseEntity<SliceResponse<SchoolFestivalV1Response>> findFestivalsBySchoolId(
        @PathVariable Long schoolId,
        @RequestParam(required = false) Long lastFestivalId,
        @RequestParam(required = false) LocalDate lastStartDate,
        @RequestParam(defaultValue = "false") Boolean isPast,
        @Parameter(description = "0 < size <= 20") @RequestParam(defaultValue = "10") int size
    ) {
        LocalDate today = LocalDate.now();
        var searchCondition = new SchoolFestivalV1SearchCondition(lastFestivalId, lastStartDate, isPast,
            PageRequest.ofSize(size));
        Slice<SchoolFestivalV1Response> response = schoolV1QueryService.findFestivalsBySchoolId(
            schoolId,
            today,
            searchCondition
        );
        return ResponseEntity.ok(SliceResponse.from(response));
    }
}
