package com.festago.festival.dto.event;

import com.festago.festival.domain.Festival;

public record FestivalCreatedEvent(
    Festival festival
) {

}
