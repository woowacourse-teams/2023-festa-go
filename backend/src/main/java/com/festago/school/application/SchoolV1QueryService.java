package com.festago.school.application;

import com.festago.school.presentation.v1.dto.SchoolV1Response;
import com.festago.school.presentation.v1.dto.SchoolsV1Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolV1QueryService {

    public SchoolsV1Response findAll(String searchFilter, String searchKeyword, Pageable pageable) {
        return null;
    }

    public SchoolV1Response findById(Long schoolId) {
        return null;
    }
}
