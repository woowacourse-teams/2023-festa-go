package com.festago.festival.dto;

import java.util.List;

public record FestivalV1ListResponse(
    boolean isLastPage,
    List<FestivalV1Response> festivals
) {

}
