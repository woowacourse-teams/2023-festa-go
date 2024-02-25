package com.festago.stage.application.command;

import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.stage.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StageCreateService {

    private final StageRepository stageRepository;

    public Long createStage(StageCreateCommand command) {
        return null;
    }
}
