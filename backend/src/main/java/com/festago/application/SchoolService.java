package com.festago.application;

import com.festago.domain.SchoolRepository;
import com.festago.dto.SchoolsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public SchoolsResponse findAll() {
        return SchoolsResponse.from(schoolRepository.findAll());
    }
}
