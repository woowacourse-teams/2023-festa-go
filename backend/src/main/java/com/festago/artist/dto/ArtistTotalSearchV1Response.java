package com.festago.artist.dto;

public record ArtistTotalSearchV1Response(
    Long id,
    String name,
    String profileImageUrl,
    Integer countOfTodayStage,
    Integer countOfPlannedStage
) {

    public static ArtistTotalSearchV1Response of(ArtistSearchV1Response artistResponse, ArtistSearchStageCountV1Response stageCount) {
        return new ArtistTotalSearchV1Response(
            artistResponse.id(),
            artistResponse.name(),
            artistResponse.profileImageUrl(),
            stageCount.countOfTodayStage(),
            stageCount.countOfPlannedStage()
        );
    }
}
