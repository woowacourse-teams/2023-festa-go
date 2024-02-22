package com.festago.festival.domain.validator;

import com.festago.festival.dto.command.FestivalUpdateCommand;

public interface FestivalUpdateValidator {

    void validate(Long festivalId, FestivalUpdateCommand command);
}
