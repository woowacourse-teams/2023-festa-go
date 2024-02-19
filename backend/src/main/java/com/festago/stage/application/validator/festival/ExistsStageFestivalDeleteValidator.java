package com.festago.stage.application.validator.festival;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.application.validator.FestivalDeleteValidator;
import com.festago.stage.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExistsStageFestivalDeleteValidator implements FestivalDeleteValidator {

    private final StageRepository stageRepository;

    @Override
    public void validate(Long festivalId) {
        if (stageRepository.existsByFestivalId(festivalId)) {
            throw new BadRequestException(ErrorCode.FESTIVAL_DELETE_CONSTRAINT_EXISTS_STAGE);
        }
    }
}
