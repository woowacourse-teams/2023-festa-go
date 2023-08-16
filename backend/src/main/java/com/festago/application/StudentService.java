package com.festago.application;

import com.festago.domain.MailClient;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.School;
import com.festago.domain.SchoolRepository;
import com.festago.domain.StudentCode;
import com.festago.domain.StudentCodeRepository;
import com.festago.domain.StudentRepository;
import com.festago.domain.VerificationCode;
import com.festago.domain.VerificationCodeProvider;
import com.festago.domain.VerificationMailPayload;
import com.festago.dto.StudentSendMailRequest;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
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

    public StudentService(MailClient mailClient, VerificationCodeProvider codeProvider,
                          StudentCodeRepository studentCodeRepository, SchoolRepository schoolRepository,
                          MemberRepository memberRepository, StudentRepository studentRepository) {
        this.mailClient = mailClient;
        this.codeProvider = codeProvider;
        this.studentCodeRepository = studentCodeRepository;
        this.schoolRepository = schoolRepository;
        this.memberRepository = memberRepository;
        this.studentRepository = studentRepository;
    }

    public void sendVerificationMail(Long memberId, StudentSendMailRequest request) {
        validateStudent(memberId);
        validateDuplicateEmail(request);
        Member member = findMember(memberId);
        School school = findSchool(request.schoolId());
        VerificationCode code = codeProvider.provide();
        studentCodeRepository.findByMember(member).ifPresentOrElse(
            studentCode -> studentCode.updateCode(code, school),
            () -> studentCodeRepository.save(new StudentCode(code, member, school))
        );

        mailClient.send(new VerificationMailPayload(code, request.username(), school.getDomain()));
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

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private School findSchool(Long schoolId) {
        return schoolRepository.findById(schoolId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
    }
}
