package com.festago.presentation;

import com.festago.application.StudentService;
import com.festago.auth.annotation.Member;
import com.festago.dto.StudentSendMailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
@Tag(name = "학생 요청")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/send-verification")
    @Operation(description = "학교 인증 이메일을 전송한다.", summary = "학생 인증 이메일 전송")
    public ResponseEntity<Void> sendEmail(@Member Long memberId,
                                          @RequestBody StudentSendMailRequest request) {
        studentService.sendVerificationMail(memberId, request);
        return ResponseEntity.ok()
            .build();
    }
}
