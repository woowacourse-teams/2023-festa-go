package com.festago.student.presentation;

import com.festago.auth.annotation.Member;
import com.festago.student.application.StudentService;
import com.festago.student.dto.StudentResponse;
import com.festago.student.dto.StudentSendMailRequest;
import com.festago.student.dto.StudentVerificateRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated(forRemoval = true)
@Hidden
@RestController
@RequestMapping("/students")
@Tag(name = "학생 요청")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/send-verification")
    @Operation(description = "학교 인증 이메일을 전송한다.", summary = "학생 인증 이메일 전송")
    public ResponseEntity<Void> sendEmail(@Member Long memberId,
                                          @RequestBody @Valid StudentSendMailRequest request) {
        studentService.sendVerificationMail(memberId, request);
        return ResponseEntity.ok()
            .build();
    }

    @PostMapping("/verification")
    @Operation(description = "학교 인증을 수행한다.", summary = "학생 인증 수행")
    public ResponseEntity<Void> verify(@Member Long memberId,
                                       @RequestBody @Valid StudentVerificateRequest request) {
        studentService.verify(memberId, request);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping
    @Operation(description = "학생 인증 정보를 조회한다.", summary = "학생 인증 정보 조회")
    public ResponseEntity<StudentResponse> findVerification(@Member Long memberId) {
        StudentResponse response = studentService.findVerification(memberId);
        return ResponseEntity.ok()
            .body(response);
    }
}
