package com.festago.presentation;

import com.festago.application.StudentService;
import com.festago.auth.domain.Login;
import com.festago.auth.dto.LoginMember;
import com.festago.dto.StudentSendMailRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/send-verification")
    public ResponseEntity<Void> sendEmail(@Login LoginMember loginMember,
                                          @RequestBody StudentSendMailRequest request) {
        studentService.sendVerificationMail(loginMember.memberId(), request);
        return ResponseEntity.ok()
            .build();
    }
}
