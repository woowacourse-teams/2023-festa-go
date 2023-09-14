package com.festago.stage.application;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.dto.StageCreateRequest;
import com.festago.stage.dto.StageResponse;
import com.festago.stage.repository.StageRepository;
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
        Stage newStage = stageRepository.save(new Stage(
            request.startTime(),
            request.lineUp(),
            request.ticketOpenTime(),
            festival));

        return StageResponse.from(newStage);
    }

    private Festival findFestivalById(Long festivalId) {
        return festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
    }
}
