package com.festago.festival.dto.v1;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalInfo;
import com.festago.festival.domain.FestivalInfoConverter;
import java.util.List;

public record FestivalV1ListResponse(
    boolean isLastPage,
    List<FestivalV1Response> festivals

) {

    public static FestivalV1ListResponse of(
        boolean isLastPage,
        List<Festival> festivals,
        List<FestivalInfo> festivalInfos,
        FestivalInfoConverter infoConverter) {
        List<FestivalV1Response> festivalV1Responses = festivals.stream()
            .map(festival -> toFestivalV1Response(festival, festivalInfos, infoConverter))
            .toList();
        return new FestivalV1ListResponse(isLastPage, festivalV1Responses);
    }

    private static FestivalV1Response toFestivalV1Response(
        Festival festival,
        List<FestivalInfo> festivalInfos,
        FestivalInfoConverter infoConverter) {
        FestivalInfo info = festivalInfos.stream()
            .filter(festivalInfo -> festivalInfo.getFestivalId().equals(festival.getId()))
            .findAny()
            .orElseThrow(() -> new InternalServerException(ErrorCode.FESTIVAL_INFO_ERROR));
        return new FestivalV1Response(
            festival.getId(),
            festival.getName(),
            festival.getStartDate(),
            festival.getEndDate(),
            festival.getThumbnail(),
            SchoolV1Response.from(festival.getSchool()),
            makeArtistResponse(info.getArtistInfo(), infoConverter)
        );
    }

    private static List<ArtistV1Response> makeArtistResponse(String artistInfo, FestivalInfoConverter infoConverter) {
        return infoConverter.convert(artistInfo).stream()
            .map(ArtistV1Response::from)
            .toList();
    }
}
