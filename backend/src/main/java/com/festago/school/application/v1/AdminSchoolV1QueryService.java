package com.festago.school.application.v1;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.querydsl.SearchCondition;
import com.festago.school.dto.v1.AdminSchoolV1Response;
import com.festago.school.repository.v1.AdminSchoolV1QueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminSchoolV1QueryService {

    private final AdminSchoolV1QueryDslRepository schoolQueryDslRepository;

    public Page<AdminSchoolV1Response> findAll(SearchCondition searchCondition) {
        return schoolQueryDslRepository.findAll(searchCondition);
    }

    public AdminSchoolV1Response findById(Long schoolId) {
        return schoolQueryDslRepository.findById(schoolId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
    }
}
