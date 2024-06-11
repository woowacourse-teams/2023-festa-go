package com.festago.stage.application.command;

import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.util.Validator;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.validator.StageUpdateValidator;
import com.festago.stage.dto.command.StageUpdateCommand;
import com.festago.stage.dto.event.StageUpdatedEvent;
import com.festago.stage.repository.StageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StageUpdateService {

    private static final int MAX_ARTIST_SIZE = 10;

    private final StageRepository stageRepository;
    private final ArtistRepository artistRepository;
    private final List<StageUpdateValidator> validators;
    private final ApplicationEventPublisher eventPublisher;

    public void updateStage(Long stageId, StageUpdateCommand command) {
        validate(command);
        LocalDateTime startTime = command.startTime();
        LocalDateTime ticketOpenTime = command.ticketOpenTime();
        List<Long> artistIds = command.artistIds();
        Stage stage = stageRepository.findByIdWithFetch(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
        stage.changeTime(startTime, ticketOpenTime);
        stage.renewArtists(artistIds);
        validators.forEach(validator -> validator.validate(stage));
        eventPublisher.publishEvent(new StageUpdatedEvent(stage));
    }

    private void validate(StageUpdateCommand command) {
        List<Long> artistIds = command.artistIds();
        Validator.maxSize(artistIds, MAX_ARTIST_SIZE, "artistIds");
        Validator.notDuplicate(artistIds, "artistIds");
        if (artistRepository.countByIdIn(artistIds) != artistIds.size()) {
            throw new NotFoundException(ErrorCode.ARTIST_NOT_FOUND);
        }
    }
}
