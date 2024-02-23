package com.festago.festival.application.command;

import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.festival.dto.command.FestivalUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalCommandFacadeService {

    private final FestivalCreateService festivalCreateService;
    private final FestivalUpdateService festivalUpdateService;
    private final FestivalDeleteService festivalDeleteService;

    public Long createFestival(FestivalCreateCommand command) {
        return festivalCreateService.createFestival(command);
    }

    public void updateFestival(Long festivalId, FestivalUpdateCommand command) {
        festivalUpdateService.updateFestival(festivalId, command);
    }

    public void deleteFestival(Long festivalId) {
        festivalDeleteService.deleteFestival(festivalId);
    }
}
