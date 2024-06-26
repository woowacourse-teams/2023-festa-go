package com.festago.school.application;

import com.festago.school.domain.validator.SchoolDeleteValidator;
import com.festago.school.dto.event.SchoolDeletedEvent;
import com.festago.school.repository.SchoolRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolDeleteService {

    private final SchoolRepository schoolRepository;
    private final List<SchoolDeleteValidator> validators;
    private final ApplicationEventPublisher eventPublisher;

    public void deleteSchool(Long schoolId) {
        validators.forEach(validator -> validator.validate(schoolId));
        schoolRepository.deleteById(schoolId);
        eventPublisher.publishEvent(new SchoolDeletedEvent(schoolId));
    }
}
