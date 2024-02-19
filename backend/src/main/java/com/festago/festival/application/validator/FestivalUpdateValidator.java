package com.festago.festival.application.validator;

import com.festago.festival.dto.command.FestivalUpdateCommand;

public interface FestivalUpdateValidator {

    void validate(Long festivalId, FestivalUpdateCommand command);
}
