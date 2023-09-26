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
import com.festago.student.domain.VerificationMailPayload;
import com.festago.student.dto.StudentSendMailRequest;
import com.festago.student.dto.StudentVerificateRequest;
import com.festago.student.repository.StudentCodeRepository;
import com.festago.student.repository.StudentRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {

    private final MailClient mailClient;
    private final VerificationCodeProvider codeProvider;
    private final StudentCodeRepository studentCodeRepository;
    private final SchoolRepository schoolRepository;
    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final Clock clock;

    public StudentService(MailClient mailClient, VerificationCodeProvider codeProvider,
                          StudentCodeRepository studentCodeRepository, SchoolRepository schoolRepository,
                          MemberRepository memberRepository, StudentRepository studentRepository, Clock clock) {
        this.mailClient = mailClient;
        this.codeProvider = codeProvider;
        this.studentCodeRepository = studentCodeRepository;
        this.schoolRepository = schoolRepository;
        this.memberRepository = memberRepository;
        this.studentRepository = studentRepository;
        this.clock = clock;
    }

    public void sendVerificationMail(Long memberId, StudentSendMailRequest request) {
        Member member = findMember(memberId);
        School school = findSchool(request.schoolId());
        validate(memberId, request);
        VerificationCode code = codeProvider.provide();
        studentCodeRepository.deleteByMember(member);
        studentCodeRepository.save(new StudentCode(code, school, member, request.username()));
        mailClient.send(new VerificationMailPayload(code, request.username(), school.getDomain()));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private School findSchool(Long schoolId) {
        return schoolRepository.findById(schoolId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
    }

    private void validate(Long memberId, StudentSendMailRequest request) {
        validateFrequentRequest(memberId);
        validateStudent(memberId);
        validateDuplicateEmail(request);
    }

    private void validateFrequentRequest(Long memberId) {
        studentCodeRepository.findByMemberId(memberId)
            .ifPresent(code -> {
                if (!code.canReissue(LocalDateTime.now(clock))) {
                    throw new BadRequestException(ErrorCode.TOO_FREQUENT_REQUESTS);
                }
            });
    }

    private void validateStudent(Long memberId) {
        if (studentRepository.existsByMemberId(memberId)) {
            throw new BadRequestException(ErrorCode.ALREADY_STUDENT_VERIFIED);
        }
    }

    private void validateDuplicateEmail(StudentSendMailRequest request) {
        if (studentRepository.existsByUsernameAndSchoolId(request.username(), request.schoolId())) {
            throw new BadRequestException(ErrorCode.DUPLICATE_STUDENT_EMAIL);
        }
    }

    public void verificate(Long memberId, StudentVerificateRequest request) {
        validateStudent(memberId);
        Member member = findMember(memberId);
        StudentCode studentCode = studentCodeRepository.findByCodeAndMember(new VerificationCode(request.code()),
                member)
            .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_STUDENT_VERIFICATION_CODE));
        studentRepository.save(new Student(member, studentCode.getSchool(), studentCode.getUsername()));
        studentCodeRepository.deleteByMember(member);
    }
}
