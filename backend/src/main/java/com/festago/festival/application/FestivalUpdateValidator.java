package com.festago.festival.application;

import com.festago.festival.dto.command.FestivalUpdateCommand;

public interface FestivalUpdateValidator {

    void validate(Long festivalId, FestivalUpdateCommand command);
}
