package com.festago.school.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.school.domain.School;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.school.dto.SchoolUpdateCommand;
import com.festago.school.repository.SchoolRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolCommandService {

    private final SchoolRepository schoolRepository;

    public Long createSchool(SchoolCreateCommand command) {
        validateCreate(command);
        School school = schoolRepository.save(command.toDomain());
        return school.getId();
    }

    private void validateCreate(SchoolCreateCommand command) {
        String domain = command.domain();
        String name = command.name();
        if (schoolRepository.existsByDomain(domain)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SCHOOL_DOMAIN);
        }
        if (schoolRepository.existsByName(name)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SCHOOL_NAME);
        }
    }

    /**
     * TODO 학교를 인증한 학생이 있다면, domain을 변경하는 것은 문제가 될 수 있지 않을까?
     */
    public void updateSchool(Long schoolId, SchoolUpdateCommand command) {
        School school = schoolRepository.getOrThrow(schoolId);
        validateUpdate(school, command);
        school.changeName(command.name());
        school.changeDomain(command.domain());
        school.changeRegion(command.region());
        school.changeLogoUrl(command.logoUrl());
        school.changeBackgroundImageUrl(command.backgroundImageUrl());
    }

    private void validateUpdate(School school, SchoolUpdateCommand command) {
        String domain = command.domain();
        String name = command.name();
        if (!Objects.equals(school.getDomain(), domain) && schoolRepository.existsByDomain(domain)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SCHOOL_DOMAIN);
        }
        if (!Objects.equals(school.getName(), name) && schoolRepository.existsByName(name)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SCHOOL_NAME);
        }
    }
}
