package com.festago.school.presentation.v1;

import com.festago.common.aop.ValidPageable;
import com.festago.common.querydsl.SearchCondition;
import com.festago.school.application.SchoolV1QueryService;
import com.festago.school.presentation.v1.dto.SchoolV1Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final SchoolV1QueryService schoolQueryService;

    @GetMapping
    @ValidPageable(maxSize = 50)
    public ResponseEntity<Page<SchoolV1Response>> findAllSchools(
        @RequestParam(defaultValue = "") String searchFilter,
        @RequestParam(defaultValue = "") String searchKeyword,
        Pageable pageable
    ) {
        return ResponseEntity.ok()
            .body(schoolQueryService.findAll(new SearchCondition(searchFilter, searchKeyword, pageable)));
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolV1Response> findSchoolById(
        @PathVariable Long schoolId
    ) {
        return ResponseEntity.ok()
            .body(schoolQueryService.findById(schoolId));
    }
}
