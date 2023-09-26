package com.festago.school.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.school.domain.School;
import com.festago.school.dto.SchoolCreateRequest;
import com.festago.school.dto.SchoolResponse;
import com.festago.school.dto.SchoolsResponse;
import com.festago.school.repository.SchoolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public SchoolResponse create(SchoolCreateRequest request) {
        validateSchool(request);
        School school = schoolRepository.save(new School(request.domain(), request.name()));
        return SchoolResponse.from(school);
    }

    private void validateSchool(SchoolCreateRequest request) {
        if (schoolRepository.existsByDomainOrName(request.domain(), request.name())) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SCHOOL);
        }
    }

    @Transactional(readOnly = true)
    public SchoolsResponse findAll() {
        return SchoolsResponse.from(schoolRepository.findAll());
    }
}
