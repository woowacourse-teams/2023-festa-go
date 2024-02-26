package com.festago.stage.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.dto.event.StageCreatedEvent;
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

    private List<Artist> getStageArtists(Long stageId) {
        Set<Long> artistIds = stageArtistRepository.findAllArtistIdByStageId(stageId);
        List<Artist> artists = artistRepository.findByIdIn(artistIds);
        if (artists.size() != artistIds.size()) {
            // TODO EventListener에서 예외가 나면 예외를 던질 필요가 있을까?
            // 지금은 동기로 진행되니 의미가 있겠지만, 비동기로 로직이 변경된다면?
            log.error("StageArtist에 존재하지 않은 Artist가 있습니다. artistsIds=" + artistIds);
            throw new NotFoundException(ErrorCode.ARTIST_NOT_FOUND);
        }
        return artists;
    }
}
