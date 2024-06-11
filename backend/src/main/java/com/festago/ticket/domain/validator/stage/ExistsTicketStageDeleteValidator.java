package com.festago.ticket.domain.validator.stage;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.validator.StageDeleteValidator;
import com.festago.ticket.repository.StageTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistsTicketStageDeleteValidator implements StageDeleteValidator {

    private final StageTicketRepository stageTicketRepository;

    @Override
    public void validate(Stage stage) {
        if (stageTicketRepository.existsByStageId(stage.getId())) {
            throw new BadRequestException(ErrorCode.STAGE_DELETE_CONSTRAINT_EXISTS_TICKET);
        }
    }
}
