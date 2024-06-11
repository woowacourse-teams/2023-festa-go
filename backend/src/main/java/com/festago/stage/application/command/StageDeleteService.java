package com.festago.stage.application.command;

import com.festago.stage.domain.validator.StageDeleteValidator;
import com.festago.stage.dto.event.StageDeletedEvent;
import com.festago.stage.repository.StageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StageDeleteService {

    private final StageRepository stageRepository;
    private final List<StageDeleteValidator> validators;
    private final ApplicationEventPublisher eventPublisher;

    public void deleteStage(Long stageId) {
        stageRepository.findById(stageId).ifPresent(stage -> {
            validators.forEach(validator -> validator.validate(stage));
            stageRepository.deleteById(stageId);
            eventPublisher.publishEvent(new StageDeletedEvent(stage));
        });
    }
}
