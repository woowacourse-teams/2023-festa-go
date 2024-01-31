package com.festago.festival.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.festival.dto.command.FestivalUpdateCommand;
import com.festago.festival.dto.event.FestivalCreatedEvent;
import com.festago.festival.dto.event.FestivalDeletedEvent;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalCommandService {

    private final FestivalRepository festivalRepository;
    private final SchoolRepository schoolRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final List<FestivalDeleteValidator> festivalDeleteValidators;
    private final Clock clock;

    public Long createFestival(FestivalCreateCommand command) {
        School school = schoolRepository.getOrThrow(command.schoolId());
        Festival festival = command.toEntity(school);
        validateCreate(festival);
        festivalRepository.save(festival);
        eventPublisher.publishEvent(new FestivalCreatedEvent(festival.getId()));
        return festival.getId();
    }

    private void validateCreate(Festival festival) {
        if (festival.isBeforeStartDate(LocalDate.now(clock))) {
            throw new BadRequestException(ErrorCode.INVALID_FESTIVAL_START_DATE);
        }
    }

    /**
     * 강제로 수정할 일이 필요할 수 있으므로, 시작일이 과거여도 예외를 발생하지 않음
     */
    public void updateFestival(Long festivalId, FestivalUpdateCommand command) {
        Festival festival = festivalRepository.getOrThrow(festivalId);
        festival.changeName(command.name());
        festival.changeThumbnail(command.thumbnail());
        festival.changeDate(command.startDate(), command.endDate());
    }

    public void deleteFestival(Long festivalId) {
        festivalDeleteValidators.forEach(validator -> validator.validate(festivalId));
        festivalRepository.deleteById(festivalId);
        eventPublisher.publishEvent(new FestivalDeletedEvent(festivalId));
    }
}
