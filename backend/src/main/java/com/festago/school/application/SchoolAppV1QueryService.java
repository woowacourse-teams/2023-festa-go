package com.festago.school.application;

import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.repository.v1.SchoolAppV1QueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolAppV1QueryService {

    private final SchoolAppV1QueryDslRepository schoolAppV1QueryDslRepository;

    public SchoolDetailV1Response findById(Long schoolId) {
        return schoolAppV1QueryDslRepository.findById(schoolId);
    }
}
