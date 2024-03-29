package com.festago.stage.application;

import com.festago.festival.application.FestivalQueryInfoArtistRenewService;
import com.festago.stage.domain.Stage;
import com.festago.stage.dto.event.StageCreatedEvent;
import com.festago.stage.dto.event.StageDeletedEvent;
import com.festago.stage.dto.event.StageUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RenewFestivalQueryInfoEventListener {

    private final FestivalQueryInfoArtistRenewService festivalQueryInfoArtistRenewService;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageCreatedEventHandler(StageCreatedEvent event) {
        Stage stage = event.stage();
        festivalQueryInfoArtistRenewService.renewArtistInfo(stage.getFestival().getId());
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageUpdatedEventHandler(StageUpdatedEvent event) {
        Stage stage = event.stage();
        festivalQueryInfoArtistRenewService.renewArtistInfo(stage.getFestival().getId());
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void stageDeletedEventHandler(StageDeletedEvent event) {
        Stage stage = event.stage();
        festivalQueryInfoArtistRenewService.renewArtistInfo(stage.getFestival().getId());
    }
}
