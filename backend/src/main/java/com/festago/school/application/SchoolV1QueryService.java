package com.festago.school.application;

import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.repository.v1.SchoolV1QueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolV1QueryService {

    private final SchoolV1QueryDslRepository schoolV1QueryDslRepository;

    public SchoolDetailV1Response findById(Long schoolId) {
        return schoolV1QueryDslRepository.findById(schoolId);
    }
}
