package com.festago.stage.application.command;

import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.util.Validator;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.stage.dto.event.StageCreatedEvent;
import com.festago.stage.repository.StageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StageCreateService {

    private static final int MAX_ARTIST_SIZE = 10;

    private final StageRepository stageRepository;
    private final FestivalRepository festivalRepository;
    private final ArtistRepository artistRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long createStage(StageCreateCommand command) {
        validate(command);
        Festival festival = festivalRepository.getOrThrow(command.festivalId());
        Stage stage = stageRepository.save(new Stage(
            command.startTime(),
            command.ticketOpenTime(),
            festival
        ));
        List<Long> artistIds = command.artistIds();
        stage.renewArtists(artistIds);
        eventPublisher.publishEvent(new StageCreatedEvent(stage));
        return stage.getId();
    }

    private void validate(StageCreateCommand command) {
        List<Long> artistIds = command.artistIds();
        Validator.maxSize(artistIds, MAX_ARTIST_SIZE, "artistIds");
        Validator.notDuplicate(artistIds, "artistIds");
        if (artistRepository.countByIdIn(artistIds) != artistIds.size()) {
            throw new NotFoundException(ErrorCode.ARTIST_NOT_FOUND);
        }
    }
}
