package com.festago.artist.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.ImageUrlHelper;
import com.festago.common.util.Validator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "profile_image_url")
    private String profileImage;

    private String backgroundImageUrl;

    public Artist(Long id, String name, String profileImage, String backgroundImageUrl) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.profileImage = ImageUrlHelper.getBlankStringIfBlank(profileImage);
        this.backgroundImageUrl = ImageUrlHelper.getBlankStringIfBlank(backgroundImageUrl);
    }

    private void validateName(String name) {
        Validator.notBlank(name, "name");
    }

    public Artist(String name, String profileImage, String backgroundImageUrl) {
        this(null, name, profileImage, backgroundImageUrl);
    }

    public void update(String name, String profileImage, String backgroundImageUrl) {
        validateName(name);
        this.name = name;
        this.profileImage = ImageUrlHelper.getBlankStringIfBlank(profileImage);
        this.backgroundImageUrl = ImageUrlHelper.getBlankStringIfBlank(backgroundImageUrl);
    }

    public List<String> getImageUrls() {
        return List.of(profileImage, backgroundImageUrl);
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
