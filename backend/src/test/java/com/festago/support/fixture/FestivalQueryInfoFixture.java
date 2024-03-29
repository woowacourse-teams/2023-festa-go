package com.festago.support.fixture;

import com.festago.festival.domain.FestivalQueryInfo;

public class FestivalQueryInfoFixture extends BaseFixture {

    private Long festivalId;
    private String artistInfo;

    private FestivalQueryInfoFixture() {
    }

    public static FestivalQueryInfoFixture builder() {
        return new FestivalQueryInfoFixture();
    }

    public FestivalQueryInfoFixture festivalId(Long festivalId) {
        this.festivalId = festivalId;
        return this;
    }

    public FestivalQueryInfoFixture artistInfo(String artistInfo) {
        this.artistInfo = artistInfo;
        return this;
    }

    public FestivalQueryInfo build() {
        FestivalQueryInfo festivalQueryInfo = FestivalQueryInfo.create(festivalId);
        festivalQueryInfo.updateArtistInfo(null, ignore -> artistInfo);
        return festivalQueryInfo;
    }
}
