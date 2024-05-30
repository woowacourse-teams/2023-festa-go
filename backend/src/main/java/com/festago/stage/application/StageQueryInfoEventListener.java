package com.festago.stage.application;

import com.festago.stage.domain.Stage;
import com.festago.stage.dto.event.StageCreatedEvent;
import com.festago.stage.dto.event.StageDeletedEvent;
import com.festago.stage.dto.event.StageUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StageQueryInfoEventListener {

    private final StageQueryInfoService stageQueryInfoService;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageCreatedEventHandler(StageCreatedEvent event) {
        Stage stage = event.stage();
        stageQueryInfoService.initialStageQueryInfo(stage);
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageUpdatedEventHandler(StageUpdatedEvent event) {
        Stage stage = event.stage();
        stageQueryInfoService.renewalStageQueryInfo(stage);
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageDeletedEventHandler(StageDeletedEvent event) {
        Stage stage = event.stage();
        stageQueryInfoService.deleteStageQueryInfo(stage.getId());
    }
}
