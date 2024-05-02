package com.festago.festival.application.command;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.festival.dto.event.FestivalCreatedEvent;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import java.time.Clock;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalCreateService {

    private final FestivalRepository festivalRepository;
    private final SchoolRepository schoolRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public Long createFestival(FestivalCreateCommand command) {
        School school = schoolRepository.getOrThrow(command.schoolId());
        Festival festival = command.toEntity(school);
        validate(festival);
        festivalRepository.save(festival);
        eventPublisher.publishEvent(new FestivalCreatedEvent(festival));
        return festival.getId();
    }

    private void validate(Festival festival) {
        if (festival.isStartDateBeforeTo(LocalDate.now(clock))) {
            throw new BadRequestException(ErrorCode.INVALID_FESTIVAL_START_DATE);
        }
    }
}
