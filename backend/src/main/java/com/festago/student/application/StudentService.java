package com.festago.student.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.student.domain.Student;
import com.festago.student.domain.StudentCode;
import com.festago.student.domain.VerificationCode;
import com.festago.student.dto.StudentResponse;
import com.festago.student.dto.StudentSendMailRequest;
import com.festago.student.dto.StudentVerificateRequest;
import com.festago.student.repository.StudentCodeRepository;
import com.festago.student.repository.StudentRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final MailClient mailClient;
    private final VerificationCodeProvider codeProvider;
    private final StudentCodeRepository studentCodeRepository;
    private final SchoolRepository schoolRepository;
    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final Clock clock;

    public void sendVerificationMail(Long memberId, StudentSendMailRequest request) {
        StudentCode studentCode = createStudentCore(memberId, request);
        validate(studentCode);
        saveStudentCodeOrReissue(studentCode);
        sendEmail(studentCode);
    }

    private StudentCode createStudentCore(Long memberId, StudentSendMailRequest request) {
        Member member = findMember(memberId);
        School school = findSchool(request.schoolId());
        return new StudentCode(codeProvider.provide(), school, member, request.username(), LocalDateTime.now(clock));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private School findSchool(Long schoolId) {
        return schoolRepository.findById(schoolId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
    }

    private void validate(StudentCode studentCode) {
        validateStudent(studentCode.getMember().getId());
        validateDuplicateEmail(studentCode.getUsername(), studentCode.getSchool().getId());
    }

    private void validateStudent(Long memberId) {
        if (studentRepository.existsByMemberId(memberId)) {
            throw new BadRequestException(ErrorCode.ALREADY_STUDENT_VERIFIED);
        }
    }

    private void validateDuplicateEmail(String username, Long schoolId) {
        if (studentRepository.existsByUsernameAndSchoolId(username, schoolId)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_STUDENT_EMAIL);
        }
    }

    private void saveStudentCodeOrReissue(StudentCode studentCode) {
        studentCodeRepository.findByMemberId(studentCode.getMember().getId())
            .ifPresentOrElse(existsCode -> {
                existsCode.reissue(studentCode);
            }, () -> {
                studentCodeRepository.save(studentCode);
            });
    }

    private void sendEmail(StudentCode studentCode) {
        mailClient.send(mail -> {
            mail.setTo(studentCode.getEmail());
            mail.setSubject("[페스타고] 학생 이메일 인증 코드");
            mail.setText("""
                페스타고 학생 이메일 인증 코드입니다.
                Code는 다음과 같습니다.
                %s
                """.formatted(studentCode.getCode()));
        });
    }

    public void verify(Long memberId, StudentVerificateRequest request) {
        validateStudent(memberId);
        Member member = findMember(memberId);
        VerificationCode verificationCode = new VerificationCode(request.code());
        StudentCode studentCode = studentCodeRepository.findByCodeAndMember(verificationCode, member)
            .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_STUDENT_VERIFICATION_CODE));
        studentRepository.save(new Student(member, studentCode.getSchool(), studentCode.getUsername()));
        studentCodeRepository.deleteByMember(member);
    }

    @Transactional(readOnly = true)
    public StudentResponse findVerification(Long memberId) {
        return studentRepository.findByMemberIdWithFetch(memberId)
            .map(StudentResponse::verified)
            .orElseGet(StudentResponse::notVerified);
    }
}
