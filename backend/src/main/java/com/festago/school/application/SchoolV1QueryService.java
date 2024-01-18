package com.festago.school.application;

import com.festago.common.querydsl.SearchCondition;
import com.festago.school.presentation.v1.dto.SchoolV1Response;
import com.festago.school.repository.SchoolV1QueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolV1QueryService {

    private final SchoolV1QueryDslRepository schoolQueryDslRepository;

    public Page<SchoolV1Response> findAll(SearchCondition searchCondition) {
        return schoolQueryDslRepository.findAll(searchCondition);
    }

    public SchoolV1Response findById(Long schoolId) {
        return schoolQueryDslRepository.findById(schoolId);
    }
}
