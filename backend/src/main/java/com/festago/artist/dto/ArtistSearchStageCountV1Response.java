package com.festago.artist.dto;

public record ArtistSearchStageCountV1Response(
    Integer countOfTodayStage,
    Integer countOfPlannedStage
) {

}
