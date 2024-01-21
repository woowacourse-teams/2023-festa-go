package com.festago.school.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.school.dto.SchoolUpdateCommand;
import com.festago.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolCommandService {

    private final SchoolRepository schoolRepository;

    public Long createSchool(SchoolCreateCommand command) {
        String domain = command.domain();
        String name = command.name();
        SchoolRegion region = command.region();
        validateExistsDomainOrName(domain, name);
        School school = schoolRepository.save(new School(domain, name, region));
        return school.getId();
    }

    private void validateExistsDomainOrName(String domain, String name) {
        if (schoolRepository.existsByDomain(domain)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SCHOOL_DOMAIN);
        }
        if (schoolRepository.existsByName(name)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SCHOOL_NAME);
        }
    }

    /**
     * 학교를 인증한 학생이 있다면, domain을 변경하는 것은 문제가 될 수 있지 않을까?
     */
    public void updateSchool(Long schoolId, SchoolUpdateCommand command) {
        validateExistsDomainOrName(command.domain(), command.name());
        School school = schoolRepository.getOrThrow(schoolId);
        school.changeName(command.name());
        school.changeDomain(command.domain());
        // TODO School 도메인 이슈에서 주석 해제할 것!
        // school.changeRegion(command.region());
    }
}
