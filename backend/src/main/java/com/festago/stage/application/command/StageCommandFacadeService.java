package com.festago.stage.application.command;

import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.stage.dto.command.StageUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StageCommandFacadeService {

    private final StageCreateService stageCreateService;
    private final StageUpdateService stageUpdateService;
    private final StageDeleteService stageDeleteService;

    public Long createStage(StageCreateCommand command) {
        return stageCreateService.createStage(command);
    }

    public void updateStage(Long stageId, StageUpdateCommand command) {
        stageUpdateService.updateStage(stageId, command);
    }

    public void deleteStage(Long stageId) {
        stageDeleteService.deleteStage(stageId);
    }
}
