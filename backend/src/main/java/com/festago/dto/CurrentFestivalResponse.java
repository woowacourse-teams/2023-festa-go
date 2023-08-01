package com.festago.dto;

import com.festago.domain.Festival;

public record CurrentFestivalResponse(Long id,
                                      String name,
                                      String thumbnail) {

    public static CurrentFestivalResponse from(Festival festival) {
        return new CurrentFestivalResponse(
            festival.getId(),
            festival.getName(),
            festival.getThumbnail()
        );
    }
}
