package com.festago.artist.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist {

    private static final String DEFAULT_URL = "https://picsum.photos/536/354";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String profileImage;

    private String backgroundImageUrl;

    public Artist(Long id, String name, String profileImage, String backgroundImageUrl) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public Artist(String name, String profileImage) {
        this(null, name, profileImage, DEFAULT_URL);
    }

    public Artist(String name, String profileImage, String backgroundImageUrl) {
        this(null, name, profileImage, backgroundImageUrl);
    }

    public void update(String name, String profileImage, String backgroundImageUrl) {
        this.name = name;
        this.profileImage = profileImage;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }
}
