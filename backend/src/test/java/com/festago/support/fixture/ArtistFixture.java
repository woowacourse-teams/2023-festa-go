package com.festago.support.fixture;

import com.festago.artist.domain.Artist;

public class ArtistFixture extends BaseFixture {

    private Long id;
    private String name;
    private String profileImageUrl = "https://www.festago-image.com/profile.png";
    private String backgroundImageUrl = "https://www.festago-image.com/background.png";

    public static ArtistFixture builder() {
        return new ArtistFixture();
    }

    public ArtistFixture id(Long id) {
        this.id = id;
        return this;
    }

    public ArtistFixture name(String name) {
        this.name = name;
        return this;
    }

    public ArtistFixture profileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }

    public ArtistFixture backgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
        return this;
    }

    public Artist build() {
        return new Artist(
            id,
            uniqueValue("아티스트", name),
            profileImageUrl,
            backgroundImageUrl
        );
    }
}
