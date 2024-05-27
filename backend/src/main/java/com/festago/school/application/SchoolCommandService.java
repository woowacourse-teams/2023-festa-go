package com.festago.school.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.school.domain.School;
import com.festago.school.dto.command.SchoolCreateCommand;
import com.festago.school.dto.command.SchoolUpdateCommand;
import com.festago.school.dto.event.SchoolCreatedEvent;
import com.festago.school.dto.event.SchoolUpdatedEvent;
import com.festago.school.repository.SchoolRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolCommandService {

    private final SchoolRepository schoolRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long createSchool(SchoolCreateCommand command) {
        validateCreate(command);
        School school = schoolRepository.save(command.toEntity());
        eventPublisher.publishEvent(new SchoolCreatedEvent(school));
        return school.getId();
    }

    private void validateCreate(SchoolCreateCommand command) {
        String name = command.name();
        if (schoolRepository.existsByName(name)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SCHOOL_NAME);
        }
    }

    public void updateSchool(Long schoolId, SchoolUpdateCommand command) {
        School school = schoolRepository.getOrThrow(schoolId);
        validateUpdate(school, command);
        school.changeName(command.name());
        school.changeDomain(command.domain());
        school.changeRegion(command.region());
        school.changeLogoUrl(command.logoUrl());
        school.changeBackgroundImageUrl(command.backgroundImageUrl());
        eventPublisher.publishEvent(new SchoolUpdatedEvent(school));
    }

    private void validateUpdate(School school, SchoolUpdateCommand command) {
        String name = command.name();
        if (!Objects.equals(school.getName(), name) && schoolRepository.existsByName(name)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SCHOOL_NAME);
        }
    }
}
