package com.festago.ticketing.dto;

import com.festago.festival.domain.Festival;

public record MemberTicketFestivalResponse(
    Long id,
    String name,
    String thumbnail) {

    public static MemberTicketFestivalResponse from(Festival festival) {
        return new MemberTicketFestivalResponse(
            festival.getId(),
            festival.getName(),
            festival.getThumbnail());
    }
}
