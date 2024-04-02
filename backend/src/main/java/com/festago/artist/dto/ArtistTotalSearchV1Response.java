package com.festago.artist.dto;

public record ArtistTotalSearchV1Response(
    Long id,
    String name,
    String profileImageUrl,
    Integer todayStage,
    Integer plannedStage
) {

    public static ArtistTotalSearchV1Response of(ArtistSearchV1Response artistResponse,
                                                 ArtistSearchStageCountV1Response stageCount) {
        return new ArtistTotalSearchV1Response(
            artistResponse.id(),
            artistResponse.name(),
            artistResponse.profileImageUrl(),
            stageCount.todayStage(),
            stageCount.plannedStage()
        );
    }
}
