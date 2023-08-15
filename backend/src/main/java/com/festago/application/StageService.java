package com.festago.application;

import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.dto.StageCreateRequest;
import com.festago.dto.StageResponse;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StageService {

    private final StageRepository stageRepository;
    private final FestivalRepository festivalRepository;

    public StageService(StageRepository stageRepository, FestivalRepository festivalRepository) {
        this.stageRepository = stageRepository;
        this.festivalRepository = festivalRepository;
    }

    public StageResponse create(StageCreateRequest request) {
        Festival festival = findFestivalById(request.festivalId());
        Stage newStage = stageRepository.save(toStageEntity(request, festival));
        return StageResponse.from(newStage);
    }

    private Stage toStageEntity(StageCreateRequest request, Festival festival) {
        return Objects.isNull(request.lineUp()) ?
            new Stage(request.startTime(), request.ticketOpenTime(), festival) :
            new Stage(request.startTime(), request.lineUp(), request.ticketOpenTime(), festival);
    }

    private Festival findFestivalById(Long festivalId) {
        return festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
    }
}
