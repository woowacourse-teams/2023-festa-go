package com.festago.admin.application;

import com.festago.admin.dto.AdminFestivalV1Response;
import com.festago.admin.repository.AdminFestivalV1QueryDslRepository;
import com.festago.common.querydsl.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminFestivalV1QueryService {

    private final AdminFestivalV1QueryDslRepository adminFestivalV1QueryDslRepository;

    public Page<AdminFestivalV1Response> findAll(SearchCondition searchCondition) {
        return adminFestivalV1QueryDslRepository.findAll(searchCondition);
    }
}
