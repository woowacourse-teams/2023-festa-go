package com.festago.presentation;

import com.festago.school.application.SchoolService;
import com.festago.school.dto.SchoolResponse;
import com.festago.school.dto.SchoolsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<SchoolsResponse> findAll() {
        return ResponseEntity.ok()
            .body(schoolService.findAll());
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolResponse> findById(@PathVariable Long schoolId) {
        return ResponseEntity.ok()
            .body(schoolService.findById(schoolId));
    }
}
