package com.festago.stage.application.command;

import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.dto.command.StageUpdateCommand;
import com.festago.stage.dto.event.StageUpdatedEvent;
import com.festago.stage.repository.StageArtistRepository;
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

    private final StageRepository stageRepository;
    private final ArtistRepository artistRepository;
    private final StageArtistRepository stageArtistRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void updateStage(Long stageId, StageUpdateCommand command) {
        LocalDateTime startTime = command.startTime();
        LocalDateTime ticketOpenTime = command.ticketOpenTime();
        List<Long> artistIds = command.artistIds();
        Stage stage = stageRepository.findByIdWithFetch(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
        stage.changeTime(startTime, ticketOpenTime);
        renewStageArtist(stage, artistIds);
        eventPublisher.publishEvent(new StageUpdatedEvent(stage));
    }

    private void renewStageArtist(Stage stage, List<Long> artistIds) {
        if (artistRepository.countByIdIn(artistIds) != artistIds.size()) {
            throw new NotFoundException(ErrorCode.ARTIST_NOT_FOUND);
        }
        stageArtistRepository.deleteByStageId(stage.getId());
        artistIds.forEach(artistId -> stageArtistRepository.save(new StageArtist(stage.getId(), artistId)));
    }
}
