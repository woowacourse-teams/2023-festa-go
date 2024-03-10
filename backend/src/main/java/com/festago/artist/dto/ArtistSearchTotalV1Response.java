package com.festago.artist.dto;

public record ArtistSearchTotalV1Response(
    Long id,
    String name,
    String profileImageUrl,
    Integer countOfTodayStage,
    Integer countOfPlannedStage
) {

    public static ArtistSearchTotalV1Response of(ArtistSearchV1Response artistResponse, ArtistSearchStageCountV1Response stageCount) {
        return new ArtistSearchTotalV1Response(
            artistResponse.id(),
            artistResponse.name(),
            artistResponse.profileImageUrl(),
            stageCount.countOfTodayStage(),
            stageCount.countOfPlannedStage()
        );
    }
}
