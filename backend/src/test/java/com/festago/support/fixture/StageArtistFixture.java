package com.festago.support.fixture;

import com.festago.stage.domain.StageArtist;

public class StageArtistFixture extends BaseFixture {

    private Long id;
    private Long stageId;
    private Long artistId;

    public static StageArtistFixture builder(Long stageId, Long artistId) {
        StageArtistFixture stageArtistFixture = new StageArtistFixture();
        stageArtistFixture.stageId = stageId;
        stageArtistFixture.artistId = artistId;
        return stageArtistFixture;
    }

    public StageArtistFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StageArtist build() {
        return new StageArtist(
            id,
            stageId,
            artistId
        );
    }
}
