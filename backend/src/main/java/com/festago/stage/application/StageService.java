package com.festago.stage.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.dto.StageCreateRequest;
import com.festago.stage.dto.StageResponse;
import com.festago.stage.dto.StageUpdateRequest;
import com.festago.stage.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StageService {

    private final StageRepository stageRepository;
    private final FestivalRepository festivalRepository;

    public StageResponse create(StageCreateRequest request) {
        Festival festival = festivalRepository.getOrThrow(request.festivalId());
        Stage newStage = stageRepository.save(new Stage(
            request.startTime(),
            request.ticketOpenTime(),
            festival));

        return StageResponse.from(newStage);
    }

    public StageResponse findDetail(Long stageId) {
        Stage stage = findStage(stageId);
        return StageResponse.from(stage);
    }

    private Stage findStage(Long stageId) {
        return stageRepository.findById(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
    }

    public void update(Long stageId, StageUpdateRequest request) {
        Stage stage = findStage(stageId);
        stage.changeTime(request.startTime(), request.ticketOpenTime());
    }

    public void delete(Long stageId) {
        try {
            stageRepository.deleteById(stageId);
            stageRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(ErrorCode.DELETE_CONSTRAINT_STAGE);
        }
    }
}
