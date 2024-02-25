package com.festago.stage.application.command;

import com.festago.stage.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StageDeleteService {

    private final StageRepository stageRepository;

    public void deleteStage(Long stageId) {

    }
}
