package com.festago.stage.application.command;

import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.exception.ValidException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.stage.dto.event.StageCreatedEvent;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import java.util.HashSet;
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
    private final StageArtistRepository stageArtistRepository;
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
        createStageArtist(artistIds, stage);
        eventPublisher.publishEvent(new StageCreatedEvent(stage));
        return stage.getId();
    }

    private void validate(StageCreateCommand command) {
        validateMaxArtistIds(command.artistIds());
        validateDuplicateArtistIds(command.artistIds());
    }

    private void validateMaxArtistIds(List<Long> artistIds) {
        if (artistIds.size() > MAX_ARTIST_SIZE) {
            throw new ValidException("Artist의 수는 " + MAX_ARTIST_SIZE + " 이하여야 합니다.");
        }
    }

    private void validateDuplicateArtistIds(List<Long> artistIds) {
        if (new HashSet<>(artistIds).size() != artistIds.size()) {
            throw new ValidException("중복된 Artist가 존재합니다.");
        }
    }

    private void createStageArtist(List<Long> artistIds, Stage stage) {
        if (artistRepository.countByIdIn(artistIds) == artistIds.size()) {
            artistIds.forEach(artistId -> stageArtistRepository.save(new StageArtist(stage.getId(), artistId)));
        } else {
            throw new NotFoundException(ErrorCode.ARTIST_NOT_FOUND);
        }
    }
}
