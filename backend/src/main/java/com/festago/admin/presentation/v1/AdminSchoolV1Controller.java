package com.festago.admin.presentation.v1;

import com.festago.admin.application.AdminSchoolV1QueryService;
import com.festago.admin.dto.AdminSchoolV1Response;
import com.festago.admin.presentation.v1.dto.SchoolV1CreateRequest;
import com.festago.admin.presentation.v1.dto.SchoolV1UpdateRequest;
import com.festago.common.aop.ValidPageable;
import com.festago.common.querydsl.SearchCondition;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.application.SchoolDeleteService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/schools")
@RequiredArgsConstructor
@Hidden
public class AdminSchoolV1Controller {

    private final SchoolCommandService schoolCommandService;
    private final SchoolDeleteService schoolDeleteService;
    private final AdminSchoolV1QueryService schoolQueryService;

    @PostMapping
    public ResponseEntity<Void> createSchool(
        @RequestBody @Valid SchoolV1CreateRequest request
    ) {
        Long schoolId = schoolCommandService.createSchool(request.toCommand());
        return ResponseEntity.created(URI.create("/api/v1/schools/" + schoolId))
            .build();
    }

    @PatchMapping("/{schoolId}")
    public ResponseEntity<Void> updateSchool(
        @PathVariable Long schoolId,
        @RequestBody @Valid SchoolV1UpdateRequest request
    ) {
        schoolCommandService.updateSchool(schoolId, request.toCommand());
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("/{schoolId}")
    public ResponseEntity<Void> deleteSchool(
        @PathVariable Long schoolId
    ) {
        schoolDeleteService.deleteSchool(schoolId);
        return ResponseEntity.noContent()
            .build();
    }

    @GetMapping
    @ValidPageable(maxSize = 50)
    public ResponseEntity<Page<AdminSchoolV1Response>> findAllSchools(
        @RequestParam(defaultValue = "") String searchFilter,
        @RequestParam(defaultValue = "") String searchKeyword,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok()
            .body(schoolQueryService.findAll(new SearchCondition(searchFilter, searchKeyword, pageable)));
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<AdminSchoolV1Response> findSchoolById(
        @PathVariable Long schoolId
    ) {
        return ResponseEntity.ok()
            .body(schoolQueryService.findById(schoolId));
    }
}
