package com.festago.presentation;

import com.festago.school.application.SchoolService;
import com.festago.school.dto.SchoolResponse;
import com.festago.school.dto.SchoolsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schools")
@Tag(name = "학교 정보 요청")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    @Operation(description = "모든 학교 정보를 조회한다.", summary = "전체 학교 조회")
    public ResponseEntity<SchoolsResponse> findAll() {
        return ResponseEntity.ok()
            .body(schoolService.findAll());
    }

    @GetMapping("/{schoolId}")
    @Operation(description = "단일 학교 정보를 조회한다.", summary = "단일 학교 조회")
    public ResponseEntity<SchoolResponse> findById(@PathVariable Long schoolId) {
        return ResponseEntity.ok()
            .body(schoolService.findById(schoolId));
    }
}
