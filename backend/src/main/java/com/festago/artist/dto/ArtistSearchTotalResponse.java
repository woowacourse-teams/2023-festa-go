package com.festago.artist.dto;

public record ArtistSearchTotalResponse(
    Long id,
    String name,
    String profileImageUrl,
    Integer countOfTodayStage,
    Integer countOfPlannedStage
) {

    public static ArtistSearchTotalResponse of(ArtistSearchResponse artistResponse, ArtistSearchStageCount stageCount) {
        return new ArtistSearchTotalResponse(
            artistResponse.id(),
            artistResponse.name(),
            artistResponse.profileImageUrl(),
            stageCount.countOfTodayStage(),
            stageCount.countOfPlannedStage()
        );
    }
}
