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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String profileImage;

    public Artist(String name, String profileImage) {
        this(null, name, profileImage);
    }

    public Artist(Long id, String name, String profileImage) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
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

    public void update(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }
}
