package com.festago.admin.application;

import com.festago.admin.dto.school.AdminSchoolV1Response;
import com.festago.admin.repository.AdminSchoolV1QueryDslRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.querydsl.SearchCondition;
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
