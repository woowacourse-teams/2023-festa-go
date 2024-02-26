package com.festago.stage.application.command;

import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.dto.command.StageCreateCommand;
import com.festago.stage.dto.event.StageCreatedEvent;
import com.festago.stage.repository.StageArtistRepository;
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

    private final StageRepository stageRepository;
    private final FestivalRepository festivalRepository;
    private final ArtistRepository artistRepository;
    private final StageArtistRepository stageArtistRepository;
    private final ApplicationEventPublisher eventPublisher;

    // TODO 추가할 수 있는 Artist 개수에 대한 검증이 필요하지 않을까?
    public Long createStage(StageCreateCommand command) {
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

    private void createStageArtist(List<Long> artistIds, Stage stage) {
        if (artistRepository.countByIdIn(artistIds) == artistIds.size()) {
            artistIds.forEach(artistId -> stageArtistRepository.save(new StageArtist(stage.getId(), artistId)));
        } else {
            // TODO 명확한 예외를 던져주는게 좋을지?
            // ex) 존재하지 않은 아티스트 입니다. artistId = ...
            throw new NotFoundException(ErrorCode.ARTIST_NOT_FOUND);
        }
    }
}
