package com.festago.admin.presentation.v1;

import com.festago.admin.presentation.v1.dto.SchoolV1CreateRequest;
import com.festago.admin.presentation.v1.dto.SchoolV1UpdateRequest;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.application.SchoolDeleteService;
import io.swagger.v3.oas.annotations.Hidden;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/schools")
@RequiredArgsConstructor
@Hidden
public class AdminSchoolV1Controller {

    private final SchoolCommandService schoolCommandService;
    private final SchoolDeleteService schoolDeleteService;

    @PostMapping
    public ResponseEntity<Void> createSchool(
        @RequestBody SchoolV1CreateRequest request
    ) {
        Long schoolId = schoolCommandService.createSchool(request.toCommand());
        return ResponseEntity.created(URI.create("/api/v1/schools/" + schoolId))
            .build();
    }

    @PatchMapping("/{schoolId}")
    public ResponseEntity<Void> updateSchool(
        @PathVariable Long schoolId,
        @RequestBody SchoolV1UpdateRequest request
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
}
