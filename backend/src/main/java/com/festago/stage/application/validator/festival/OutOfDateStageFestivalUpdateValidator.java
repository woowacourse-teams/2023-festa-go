package com.festago.stage.application.validator.festival;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.application.validator.FestivalUpdateValidator;
import com.festago.festival.dto.command.FestivalUpdateCommand;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 축제를 수정할 때, 축제에 포함된 공연이 수정할 축제 기간의 범위를 벗어난 공연이 있는지 검증하는 클래스
 */
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OutOfDateStageFestivalUpdateValidator implements FestivalUpdateValidator {

    private final StageRepository stageRepository;

    @Override
    public void validate(Long festivalId, FestivalUpdateCommand command) {
        LocalDate festivalStartDate = command.startDate();
        LocalDate festivalEndDate = command.endDate();
        List<Stage> stages = stageRepository.findAllByFestivalId(festivalId);
        boolean isOutOfRange = stages.stream()
            .anyMatch(stage -> {
                LocalDate stageStartDate = stage.getStartTime().toLocalDate();
                return stageStartDate.isBefore(festivalStartDate) || stageStartDate.isAfter(festivalEndDate);
            });
        if (isOutOfRange) {
            throw new BadRequestException(ErrorCode.FESTIVAL_UPDATE_OUT_OF_DATE_STAGE_START_TIME);
        }
    }
}
