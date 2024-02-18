package com.festago.school.presentation.v1;

import com.festago.school.application.SchoolV1QueryService;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/school")
@Tag(name = "학교 정보 요청 V1")
@RequiredArgsConstructor
public class SchoolV1Controller {

    private final SchoolV1QueryService schoolV1QueryService;

    @GetMapping("/{schoolId}")
    @Operation(description = "학교와 해당하는 소셜미디어 정보를 함께 조회한다.", summary = "학교 상세 조회")
    public ResponseEntity<SchoolDetailV1Response> findDetailId(@PathVariable Long schoolId) {
        SchoolDetailV1Response response = schoolV1QueryService.findDetailById(schoolId);
        return ResponseEntity.ok(response);
    }
}
