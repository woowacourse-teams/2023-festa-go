package com.festago.stage.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.dto.event.StageCreatedEvent;
import com.festago.stage.dto.event.StageDeletedEvent;
import com.festago.stage.dto.event.StageUpdatedEvent;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageQueryInfoRepository;
import java.util.List;
import java.util.Set;
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

    private final StageQueryInfoRepository stageQueryInfoRepository;
    private final StageArtistRepository stageArtistRepository;
    private final ArtistRepository artistRepository;
    private final ArtistsSerializer serializer;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageCreatedEventHandler(StageCreatedEvent event) {
        Stage stage = event.stage();
        Long stageId = stage.getId();
        List<Artist> artists = getStageArtists(stageId);
        StageQueryInfo stageQueryInfo = StageQueryInfo.of(stageId, artists, serializer);
        stageQueryInfoRepository.save(stageQueryInfo);
    }

    /**
     * 해당 메서드를 사용하는 로직이 비동기로 처리된다면 예외 던지는 것을 다시 생각해볼것! (동기로 실행되면 ControllerAdvice에서 처리가 됨)
     */
    private List<Artist> getStageArtists(Long stageId) {
        Set<Long> artistIds = stageArtistRepository.findAllArtistIdByStageId(stageId);
        List<Artist> artists = artistRepository.findByIdIn(artistIds);
        if (artists.size() != artistIds.size()) {
            log.error("StageArtist에 존재하지 않은 Artist가 있습니다. artistsIds=" + artistIds);
            throw new InternalServerException(ErrorCode.ARTIST_NOT_FOUND);
        }
        return artists;
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageUpdatedEventHandler(StageUpdatedEvent event) {
        Stage stage = event.stage();
        Long stageId = stage.getId();
        List<Artist> artists = getStageArtists(stageId);
        StageQueryInfo stageQueryInfo = stageQueryInfoRepository.findByStageId(stageId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.STAGE_NOT_FOUND));
        stageQueryInfo.updateArtist(artists, serializer);
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageDeletedEventHandler(StageDeletedEvent event) {
        Stage stage = event.stage();
        stageQueryInfoRepository.deleteByStageId(stage.getId());
    }
}
