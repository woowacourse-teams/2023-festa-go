package com.festago.school.application;

import com.festago.school.dto.SchoolsResponse;
import com.festago.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public SchoolsResponse findAll() {
        return SchoolsResponse.from(schoolRepository.findAll());
    }
}
