package com.festago.stage.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.dto.event.StageCreatedEvent;
import com.festago.stage.dto.event.StageDeletedEvent;
import com.festago.stage.dto.event.StageUpdatedEvent;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RenewFestivalQueryInfoEventListener {

    private final FestivalInfoRepository festivalInfoRepository;
    private final StageRepository stageRepository;
    private final StageArtistRepository stageArtistRepository;
    private final ArtistRepository artistRepository;
    private final ArtistsSerializer serializer;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageCreatedEventHandler(StageCreatedEvent event) {
        Stage stage = event.stage();
        renewFestivalQueryInfo(stage);
    }

    private void renewFestivalQueryInfo(Stage stage) {
        Long festivalId = stage.getFestival().getId();
        List<Long> stageIds = stageRepository.findAllByFestivalId(festivalId).stream()
            .map(Stage::getId)
            .toList();
        Set<Long> artistIds = stageArtistRepository.findAllArtistIdByStageIdIn(stageIds);
        List<Artist> artists = artistRepository.findByIdIn(artistIds);
        FestivalQueryInfo festivalQueryInfo = festivalInfoRepository.findByFestivalId(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
        festivalQueryInfo.updateArtistInfo(artists, serializer);
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageUpdatedEventHandler(StageUpdatedEvent event) {
        Stage stage = event.stage();
        renewFestivalQueryInfo(stage);
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageDeletedEventHandler(StageDeletedEvent event) {
        Stage stage = event.stage();
        renewFestivalQueryInfo(stage);
    }
}
