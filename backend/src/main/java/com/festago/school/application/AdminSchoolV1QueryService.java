package com.festago.school.application;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.querydsl.SearchCondition;
import com.festago.school.presentation.v1.dto.SchoolV1Response;
import com.festago.school.repository.AdminSchoolV1QueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminSchoolV1QueryService {

    private final AdminSchoolV1QueryDslRepository schoolQueryDslRepository;

    public Page<SchoolV1Response> findAll(SearchCondition searchCondition) {
        return schoolQueryDslRepository.findAll(searchCondition);
    }

    public SchoolV1Response findById(Long schoolId) {
        return schoolQueryDslRepository.findById(schoolId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
    }
}
