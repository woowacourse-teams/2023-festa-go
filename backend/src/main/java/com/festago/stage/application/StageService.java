package com.festago.stage.application;

import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import com.festago.stage.dto.StageCreateRequest;
import com.festago.stage.dto.StageResponse;
import com.festago.stage.repository.StageRepository;
import com.festago.zfestival.domain.Festival;
import com.festago.zfestival.repository.FestivalRepository;
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
