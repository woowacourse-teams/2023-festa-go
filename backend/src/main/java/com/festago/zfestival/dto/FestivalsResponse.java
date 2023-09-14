package com.festago.zfestival.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.zfestival.domain.Festival;
import java.util.List;

public record FestivalsResponse(
    List<FestivalResponse> festivals) {

    public static FestivalsResponse from(List<Festival> festivals) {
        return festivals.stream()
            .map(FestivalResponse::from)
            .collect(collectingAndThen(toList(), FestivalsResponse::new));
    }
}
