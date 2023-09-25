package com.festago.school.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.domain.School;
import com.festago.school.dto.SchoolCreateRequest;
import com.festago.school.dto.SchoolResponse;
import com.festago.school.dto.SchoolUpdateRequest;
import com.festago.school.dto.SchoolsResponse;
import com.festago.school.repository.SchoolRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Transactional(readOnly = true)
    public SchoolsResponse findAll() {
        return SchoolsResponse.from(schoolRepository.findAll());
    }

    @Transactional(readOnly = true)
    public SchoolResponse findById(Long id) {
        return SchoolResponse.from(findSchool(id));
    }

    private School findSchool(Long id) {
        return schoolRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
    }

    public SchoolResponse create(SchoolCreateRequest request) {
        String domain = request.domain();
        String name = request.name();
        validateCreate(domain, name);
        School school = schoolRepository.save(new School(domain, name));
        return SchoolResponse.from(school);
    }

    private void validateCreate(String domain, String name) {
        if (schoolRepository.existsByDomain(domain)) {
            throw new BadRequestException(ErrorCode.EXISTS_SCHOOL_DOMAIN);
        }
        if (schoolRepository.existsByName(name)) {
            throw new BadRequestException(ErrorCode.EXISTS_SCHOOL_NAME);
        }
    }

    public void update(Long schoolId, SchoolUpdateRequest request) {
        School school = findSchool(schoolId);
        school.changeName(request.name());
        school.changeDomain(request.domain());
    }

    public void delete(Long schoolId) {
        // TODO 지금은 외래키 제약조건 때문에 참조하는 다른 엔티티가 있으면 예외가 발생하지만, 추후 이미 가입된 학생이 있다는 등 예외가 필요할듯
        try {
            schoolRepository.deleteById(schoolId);
            schoolRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(ErrorCode.DELETE_CONSTRAINT_SCHOOL);
        }
    }
}
