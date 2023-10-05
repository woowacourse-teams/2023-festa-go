package com.festago.presentation;

import com.festago.school.application.SchoolService;
import com.festago.school.dto.SchoolsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<SchoolsResponse> findAll() {
        return ResponseEntity.ok(schoolService.findAll());
    }
}
