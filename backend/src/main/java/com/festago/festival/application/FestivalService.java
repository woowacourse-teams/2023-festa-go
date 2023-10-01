package com.festago.festival.application;

import static java.util.Comparator.comparing;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.FestivalDetailResponse;
import com.festago.festival.dto.FestivalResponse;
import com.festago.festival.dto.FestivalsResponse;
import com.festago.festival.dto.event.FestivalCreateEvent;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private final StageRepository stageRepository;
    private final SchoolRepository schoolRepository;
    private final ApplicationEventPublisher publisher;

    public FestivalService(FestivalRepository festivalRepository, StageRepository stageRepository,
                           SchoolRepository schoolRepository, ApplicationEventPublisher publisher) {
        this.festivalRepository = festivalRepository;
        this.stageRepository = stageRepository;
        this.schoolRepository = schoolRepository;
        this.publisher = publisher;
    }

    public FestivalResponse create(FestivalCreateRequest request) {
        School school = schoolRepository.findById(request.schoolId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
        Festival festival = request.toEntity(school);
        validate(festival);
        Festival newFestival = festivalRepository.save(festival);
        publisher.publishEvent(new FestivalCreateEvent(newFestival.getId()));
        return FestivalResponse.from(newFestival);
    }

    private void validate(Festival festival) {
        if (!festival.canCreate(LocalDate.now())) {
            throw new BadRequestException(ErrorCode.INVALID_FESTIVAL_START_DATE);
        }
    }

    @Transactional(readOnly = true)
    public FestivalsResponse findAll() {
        List<Festival> festivals = festivalRepository.findAll();
        return FestivalsResponse.from(festivals);
    }

    @Transactional(readOnly = true)
    public FestivalDetailResponse findDetail(Long festivalId) {
        Festival festival = festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
        List<Stage> stages = stageRepository.findAllDetailByFestivalId(festivalId).stream()
            .sorted(comparing(Stage::getStartTime))
            .toList();
        return FestivalDetailResponse.of(festival, stages);
    }
}
