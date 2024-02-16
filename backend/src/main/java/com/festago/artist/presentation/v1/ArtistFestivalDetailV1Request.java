package com.festago.artist.presentation.v1;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArtistFestivalDetailV1Request {

    private final Long lastFestivalId;
    private final LocalDate lastStartDate;
    private final Boolean isPast;

    public Long getLastFestivalId() {
        return lastFestivalId;
    }

    public LocalDate getLastStartDate() {
        return lastStartDate;
    }

    public Boolean getPast() {
        return isPast;
    }
}
