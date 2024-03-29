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

    //TODO FestivalQueryInfo에 ArtistInfo를 String으로 넣어줄 수가 없음..
    public FestivalQueryInfo build() {
        return FestivalQueryInfo.create(festivalId);
    }
}
