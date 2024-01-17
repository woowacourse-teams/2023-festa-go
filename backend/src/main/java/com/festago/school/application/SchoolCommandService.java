package com.festago.school.application;

import com.festago.school.dto.SchoolCreateCommand;
import com.festago.school.dto.SchoolUpdateCommand;
import com.festago.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolCommandService {

    private final SchoolRepository schoolRepository;

    public Long createSchool(SchoolCreateCommand command) {
        // TODO
        return null;
    }

    public void updateSchool(Long schoolId, SchoolUpdateCommand command) {
        // TODO
    }
}
